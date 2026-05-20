package com.example.cherry_be.domain.device.controller;

import com.example.cherry_be.domain.device.dto.DeviceDataRequest;
import com.example.cherry_be.domain.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 라즈베리파이 데이터 수신 API
     * [POST] /api/device/data
     */
    @PostMapping("/data")
    public ResponseEntity<String> receiveData(@RequestBody DeviceDataRequest request) {
        deviceService.receiveDeviceData(request);
        return ResponseEntity.ok("데이터 수신 완료");
    }
}
