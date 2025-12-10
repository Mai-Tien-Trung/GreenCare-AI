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
public class RevenueStatsResponse {
    private Double totalRevenue; // Tổng doanh thu
    private Double revenueToday; // Doanh thu hôm nay
    private Double revenueLast7Days; // Doanh thu 7 ngày qua
    private Double revenueLast30Days; // Doanh thu 30 ngày qua
    private Long totalSuccessfulPayments; // Tổng số giao dịch thành công
    private Long totalPendingPayments; // Số giao dịch đang chờ
    private Long totalCanceledPayments; // Số giao dịch bị hủy
    private LocalDateTime lastUpdated; // Thời gian cập nhật
}
