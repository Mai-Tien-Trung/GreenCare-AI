package com.example.GreenCareAI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionStatsResponse {
    private Long totalSubscriptions; // Tổng số subscriptions
    private Long activeSubscriptions; // Số subscription đang active
    private Long expiredSubscriptions; // Số subscription đã hết hạn
    private Long freeSubscriptions; // Số subscription gói Free
    private Long premiumSubscriptions; // Số subscription gói Premium
    private Double conversionRate; // Tỷ lệ chuyển đổi Free -> Premium (%)
    private LocalDateTime lastUpdated; // Thời gian cập nhật
}
