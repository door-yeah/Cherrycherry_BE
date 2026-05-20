package com.example.cherry_be.domain.member.controller;

import com.example.cherry_be.domain.member.dto.MemberDetailResponse;
import com.example.cherry_be.domain.member.dto.MemberRegisterRequest;
import com.example.cherry_be.domain.member.dto.MemberSummaryResponse;
import com.example.cherry_be.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/targets")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 피보호자 등록
     * [POST] /api/targets
     */
    @PostMapping
    public ResponseEntity<String> registerMember(
            Authentication authentication,
            @RequestBody MemberRegisterRequest request) {
        String orgId = authentication.getName();
        Long savedId = memberService.registerMember(orgId, request);
        return ResponseEntity.ok("피보호자 등록 완료. ID: " + savedId);
    }

    /**
     * 전체 피보호자 조회 + 요약 통계
     * [GET] /api/targets
     */
    @GetMapping
    public ResponseEntity<MemberSummaryResponse> getTargets(Authentication authentication) {
        String orgId = authentication.getName();
        return ResponseEntity.ok(memberService.getTargets(orgId));
    }

    /**
     * 긴급 상태 피보호자만 조회
     * [GET] /api/targets/emergencies
     */
    @GetMapping("/emergencies")
    public ResponseEntity<List<MemberSummaryResponse.MemberInfo>> getEmergencies(
            Authentication authentication) {
        String orgId = authentication.getName();
        return ResponseEntity.ok(memberService.getEmergencies(orgId));
    }

    /**
     * 특정 피보호자 상세 조회
     * [GET] /api/targets/{targetId}
     */
    @GetMapping("/{targetId}")
    public ResponseEntity<MemberDetailResponse> getTargetDetail(
            Authentication authentication,
            @PathVariable Long targetId) {
        String orgId = authentication.getName();
        return ResponseEntity.ok(memberService.getTargetDetail(orgId, targetId));
    }
}
