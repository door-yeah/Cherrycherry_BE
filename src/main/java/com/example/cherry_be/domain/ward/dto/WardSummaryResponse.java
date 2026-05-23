package com.example.cherry_be.domain.ward.dto;

import com.example.cherry_be.domain.member.entity.Member;
import com.example.cherry_be.domain.member.entity.MemberStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WardSummaryResponse {

    private String wardName;          // 피보호자 이름
    private Integer wardAge;          // 나이
    private String wardAddress;       // 주소
    private MemberStatus status;      // 현재 상태 (SAFE / WARNING / DANGER)
    private LocalDateTime lastUpdated; // 마지막 데이터 수신 시각

    public static WardSummaryResponse from(Member member) {
        return WardSummaryResponse.builder()
                .wardName(member.getName())
                .wardAge(member.getAge() != null ? member.getAge().intValue() : null)
                .wardAddress(member.getAddress())
                .status(member.getStatus())
                .lastUpdated(member.getLastUpdated())
                .build();
    }
}
