package com.example.cherry_be.global.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청 헤더에서 Authorization 값 꺼내기
        String authHeader = request.getHeader("Authorization");

        // 2. 토큰이 없거나 "Bearer "로 시작하지 않으면 그냥 다음으로 넘김
        //    (로그인, 회원가입, Pi API 같은 permitAll 경로는 여기서 통과)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " 7글자 제거하고 순수 토큰만 추출
        String token = authHeader.substring(7);

        // 4. 토큰 유효성 검사 (만료, 위조 여부 확인)
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 토큰에서 orgId와 role 꺼내기
        String orgId = jwtUtil.getOrgId(token);
        String role = jwtUtil.getRole(token);

        // 6. 스프링 시큐리티에 "이 사람은 인증된 사람이야" 라고 등록
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        orgId,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 7. 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
