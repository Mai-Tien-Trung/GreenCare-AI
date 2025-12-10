package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.dto.response.RevenueStatsResponse;
import com.example.GreenCareAI.dto.response.SubscriptionStatsResponse;
import com.example.GreenCareAI.dto.response.UserStatsResponse;
import com.example.GreenCareAI.service.AdminAnalyticsService;
import com.example.GreenCareAI.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class AnalyticsController {

    private final LoginHistoryService loginHistoryService;
    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/logins-per-day")
    public ResponseEntity<?> loginsPerDay() {
        return ResponseEntity.ok(loginHistoryService.getLoginsPerDay());
    }

    @GetMapping("/logins-last-30-days")
    public ResponseEntity<?> loginsLast30Days() {
        return ResponseEntity.ok(Map.of(
                "count", loginHistoryService.getLoginsLast30Days()));
    }

    // 💰 Revenue Statistics
    @GetMapping("/revenue")
    public ResponseEntity<RevenueStatsResponse> getRevenueStats() {
        return ResponseEntity.ok(adminAnalyticsService.getRevenueStats());
    }

    // 👥 User Statistics
    @GetMapping("/users")
    public ResponseEntity<UserStatsResponse> getUserStats() {
        return ResponseEntity.ok(adminAnalyticsService.getUserStats());
    }

    // 📦 Subscription Statistics
    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionStatsResponse> getSubscriptionStats() {
        return ResponseEntity.ok(adminAnalyticsService.getSubscriptionStats());
    }

    // 📊 Combined Dashboard Statistics
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("revenue", adminAnalyticsService.getRevenueStats());
        dashboard.put("users", adminAnalyticsService.getUserStats());
        dashboard.put("subscriptions", adminAnalyticsService.getSubscriptionStats());
        dashboard.put("logins", Map.of(
                "last30Days", loginHistoryService.getLoginsLast30Days(),
                "perDay", loginHistoryService.getLoginsPerDay()));
        return ResponseEntity.ok(dashboard);
    }
}
