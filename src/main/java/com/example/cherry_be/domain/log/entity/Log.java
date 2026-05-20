package com.example.cherry_be.domain.log.entity;

import com.example.cherry_be.domain.member.entity.Member;
import com.example.cherry_be.domain.member.entity.MemberStatus;
import com.example.cherry_be.domain.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fall_log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization organization;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;      // 피보호자 상태 (SAFE, WARNING, DANGER)

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", nullable = false)
    private LogType logType;          // 로그 발생 이유

    @Column(name = "sensor_detail")
    private String sensorDetail;      // 어떤 센서가 고장났는지 (SENSOR_FAILURE 시에만)

    @Builder
    public Log(Member member, Organization organization, MemberStatus status,
               LogType logType, String sensorDetail) {
        this.member = member;
        this.organization = organization;
        this.detectedAt = LocalDateTime.now();
        this.status = status;
        this.logType = logType;
        this.sensorDetail = sensorDetail;
    }
}
