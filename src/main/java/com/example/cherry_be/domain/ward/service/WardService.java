package com.example.cherry_be.domain.ward.service;

import com.example.cherry_be.domain.member.entity.Member;
import com.example.cherry_be.domain.member.repository.MemberRepository;
import com.example.cherry_be.domain.user.entity.User;
import com.example.cherry_be.domain.user.repository.UserRepository;
import com.example.cherry_be.domain.ward.dto.*;
import com.example.cherry_be.domain.ward.entity.EmergencyContact;
import com.example.cherry_be.domain.ward.repository.EmergencyContactRepository;
import com.example.cherry_be.global.exception.CustomException;
import com.example.cherry_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WardService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    // 로그인한 보호자 이메일 → User → Member 조회 공통 메서드
    private User getGuardian(String oauthEmail) {
        return userRepository.findByOauthEmail(oauthEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Member getWard(User guardian) {
        return memberRepository.findByUser(guardian)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * [POST] /api/wards/me — 피보호자 등록 (보호자가 최초 1회 등록)
     */
    @Transactional
    public Long registerWard(String oauthEmail, WardRegisterRequest request) {
        User guardian = getGuardian(oauthEmail);

        // 이미 등록된 피보호자가 있으면 예외
        if (memberRepository.findByUser(guardian).isPresent()) {
            throw new CustomException(ErrorCode.WARD_ALREADY_EXISTS);
        }

        // deviceMac 중복 체크
        if (memberRepository.findByDeviceMac(request.getDeviceMac()).isPresent()) {
            throw new CustomException(ErrorCode.DEVICE_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .user(guardian)
                .organization(null)
                .name(request.getName())
                .age(request.getAge())
                .address(request.getAddress())
                .contact(request.getContact())
                .deviceMac(request.getDeviceMac())
                .build();

        return memberRepository.save(member).getId();
    }

    /**
     * [GET] /api/wards/me/summary — 홈 화면 요약 정보
     */
    @Transactional(readOnly = true)
    public WardSummaryResponse getSummary(String oauthEmail) {
        Member ward = getWard(getGuardian(oauthEmail));
        return WardSummaryResponse.from(ward);
    }

    /**
     * [GET] /api/wards/me/sensors — 센서 상태 조회
     */
    @Transactional(readOnly = true)
    public WardSensorResponse getSensors(String oauthEmail) {
        Member ward = getWard(getGuardian(oauthEmail));
        return WardSensorResponse.from(ward);
    }

    /**
     * [GET] /api/wards/me/contacts — 비상연락망 목록 조회
     */
    @Transactional(readOnly = true)
    public List<WardContactResponse> getContacts(String oauthEmail) {
        Member ward = getWard(getGuardian(oauthEmail));
        return emergencyContactRepository.findByMember(ward).stream()
                .map(WardContactResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * [POST] /api/wards/me/contacts — 비상연락망 등록
     */
    @Transactional
    public WardContactResponse addContact(String oauthEmail, WardContactRequest request) {
        Member ward = getWard(getGuardian(oauthEmail));

        EmergencyContact contact = EmergencyContact.builder()
                .member(ward)
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .relation(request.getRelation())
                .build();

        return WardContactResponse.from(emergencyContactRepository.save(contact));
    }
}
