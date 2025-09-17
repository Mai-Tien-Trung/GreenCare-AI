package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/analytics")
public class AnalyticsController {

    private final LoginHistoryService loginHistoryService;

    @GetMapping("/logins-per-day")
    public ResponseEntity<?> loginsPerDay() {
        return ResponseEntity.ok(loginHistoryService.getLoginsPerDay());
    }

    @GetMapping("/logins-last-30-days")
    public ResponseEntity<?> loginsLast30Days() {
        return ResponseEntity.ok(Map.of(
                "count", loginHistoryService.getLoginsLast30Days()
        ));
    }
}
