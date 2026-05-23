package com.example.cherry_be.domain.ward.dto;

import com.example.cherry_be.domain.ward.entity.EmergencyContact;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WardContactResponse {

    private Long id;
    private String name;        // 연락처 이름
    private String phoneNumber; // 전화번호
    private String relation;    // 관계

    public static WardContactResponse from(EmergencyContact contact) {
        return WardContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phoneNumber(contact.getPhoneNumber())
                .relation(contact.getRelation())
                .build();
    }
}
