package com.example.cherry_be.domain.ward.entity;

import com.example.cherry_be.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "emergency_contact")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 피보호자의 비상연락망인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @Column(nullable = false)
    private String name;

    // 전화번호
    @Column(nullable = false)
    private String phoneNumber;

    // 관계 (예: "딸", "아들", "며느리")
    @Column(nullable = false)
    private String relation;

    @Builder
    public EmergencyContact(Member member, String name, String phoneNumber, String relation) {
        this.member = member;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relation = relation;
    }
}
