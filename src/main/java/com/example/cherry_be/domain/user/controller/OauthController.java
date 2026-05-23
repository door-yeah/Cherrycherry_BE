package com.example.cherry_be.domain.user.controller;

import com.example.cherry_be.domain.user.dto.UserDto;
import com.example.cherry_be.domain.user.entity.User;
import com.example.cherry_be.domain.user.helper.constants.SocialLoginType;
import com.example.cherry_be.domain.user.service.OauthService;
import com.example.cherry_be.domain.user.service.UserService;
import com.example.cherry_be.global.auth.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
@Slf4j
public class OauthController {

    private final OauthService oauthService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * [GET] 구글 로그인 창으로 리다이렉트 (백엔드 직접 호출 시 사용)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            HttpServletResponse response) throws IOException {

        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        String redirectUrl = oauthService.authorize(socialLoginType);
        response.sendRedirect(redirectUrl);
    }

    /**
     * [GET] 구글 로그인 완료 후 콜백 처리 (백엔드 직접 호출 시 사용)
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public ResponseEntity<?> callback(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
                                      @RequestParam(name = "code") String code) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

        String accessToken = oauthService.requestAccessToken(socialLoginType, code);
        log.info(">> [2] 구글 Access Token 획득 성공");

        UserDto userInfo = oauthService.getUserInfo(socialLoginType, accessToken);
        log.info(">> [3] 구글 유저 정보 획득 성공 :: 이메일 = {}", userInfo.getOauthEmail());

        User user = userService.loginOrSignup(userInfo);
        log.info(">> [4] DB 저장/조회 완료 :: 회원 이름 = {}", user.getName());

        String jwtToken = jwtUtil.createToken(user.getOauthEmail(), "ROLE_USER");
        log.info(">> [5] JWT 토큰 발급 완료 :: {}", jwtToken);  // ← 추가

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken)
                .body("로그인 성공! 환영합니다 " + user.getName() + "님. (JWT 토큰이 헤더에 발급되었습니다)");
    }

    /**
     * 🔥 [POST] 프론트엔드(React)에서 구글 코드를 가로채서 서버로 던질 때 사용하는 최종 API
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> socialLoginPost(@RequestBody Map<String, String> requestBody) {
        log.info(">> 리액트로부터 POST 로그인 요청 받음 :: 데이터 = {}", requestBody);

        String code = requestBody.get("code");
        String provider = requestBody.getOrDefault("provider", "google");
        SocialLoginType socialLoginType = SocialLoginType.valueOf(provider.toUpperCase());

        String accessToken = oauthService.requestAccessToken(socialLoginType, code);
        log.info(">> [2] 구글 Access Token 획득 성공");

        UserDto userInfo = oauthService.getUserInfo(socialLoginType, accessToken);
        log.info(">> [3] 구글 유저 정보 획득 성공 :: 이메일 = {}", userInfo.getOauthEmail());

        User user = userService.loginOrSignup(userInfo);
        log.info(">> [4] DB 저장/조회 완료 :: 회원 이름 = {}", user.getName());

        String jwtToken = jwtUtil.createToken(user.getOauthEmail(), "ROLE_USER");
        log.info(">> [5] JWT 토큰 발급 완료 :: {}", jwtToken);  // ← 추가

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken)
                .body(Map.of(
                        "token", jwtToken,
                        "message", user.getName() + "님, 로그인을 환영합니다!"
                ));
    }
}
