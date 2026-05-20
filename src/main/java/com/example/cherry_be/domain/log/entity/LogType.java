package com.example.cherry_be.domain.log.entity;

public enum LogType {
    FALL_EVENT,      // 낙상 감지 상태 변화 (SAFE→DANGER 등)
    SENSOR_FAILURE   // 센서 장애 발생
}
