package com.example.cherry_be.domain.ward.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WardRegisterRequest {
    private String name;          // 피보호자 이름
    private String birthDate;     // 생년월일 (YYYY-MM-DD) → age 계산에 사용
    private String address;       // 집 주소
    private String phone;         // 전화번호 (010-XXXX-XXXX)
    private String relationship;  // 보호자와의 관계 (어머니, 아버지 등)
    private String deviceMac;     // 라즈베리파이 MAC 주소 (선택)
}
