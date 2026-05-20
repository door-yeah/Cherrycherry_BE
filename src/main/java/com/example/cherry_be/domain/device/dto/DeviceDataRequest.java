package com.example.cherry_be.domain.device.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeviceDataRequest {

    @JsonProperty("device_id")
    private String deviceId;          // 라즈베리파이 고유 ID (device_mac과 매핑)

    @JsonProperty("timestamp")
    private String timestamp;         // 감지 시간 (참고용)

    @JsonProperty("event_type")
    private String eventType;         // SAFE / WARNING / DANGER

    @JsonProperty("sensor_status")
    private SensorStatus sensorStatus;

    @Getter
    @NoArgsConstructor
    public static class SensorStatus {
        private Boolean vibrator;

        private Boolean radar;

        @JsonProperty("thermal_imaging")
        private Boolean thermalImaging;
    }
}
