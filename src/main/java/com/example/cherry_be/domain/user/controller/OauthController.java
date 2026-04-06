package com.example.cherry_be.domain.user.controller;
import com.example.cherry_be.domain.user.helper.constants.SocialLoginType;
import com.example.cherry_be.domain.user.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/auth") // 👈 잃어버린 이 줄을 꼭 다시 넣어주세요!
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        oauthService.authorize(socialLoginType);
    }

    @GetMapping(value = "/{socialLoginType}/callback")
    public String callback(@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
                           @RequestParam(name = "code") String code) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

        // 일단은 성공 메시지를 리턴해서 404가 안 뜨는지 확인합시다!
        return "로그인 성공! 코드는: " + code;
    }
}