package com.example.cherry_be.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. 비밀번호 암호화 도구를 스프링 빈(Bean)으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. 전체적인 보안 규칙 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 리액트와 통신하는 REST API 서버이므로 CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 기본 폼 로그인과 HTTP Basic 인증 비활성화 (우리는 JWT를 쓸 것이기 때문)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션을 생성하지 않음 (STATELESS 설정)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // API 주소별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 로그인, 회원가입 관련 API는 모두에게 접근 허용
                        .requestMatchers("/api/org/signup", "/api/org/login").permitAll()
                        // 그 외의 모든 요청은 토큰 인증을 거쳐야 함
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
