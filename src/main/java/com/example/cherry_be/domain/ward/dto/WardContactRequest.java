package com.example.cherry_be.domain.ward.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WardContactRequest {

    private String name;        // 연락처 이름 (예: "딸 장시온")
    private String phoneNumber; // 전화번호 (예: "010-1234-5678")
    private String relation;    // 관계 (예: "딸", "아들")
}
