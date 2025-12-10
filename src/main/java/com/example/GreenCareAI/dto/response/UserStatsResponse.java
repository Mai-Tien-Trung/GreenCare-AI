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
public class UserStatsResponse {
    private Long totalUsers; // Tổng số người dùng
    private Long totalAdmins; // Số admin
    private Long totalRegularUsers; // Số user thường
    private Long newUsersToday; // Số user mới hôm nay
    private Long newUsersLast7Days; // Số user mới 7 ngày qua
    private Long newUsersLast30Days; // Số user mới 30 ngày qua
    private LocalDateTime lastUpdated; // Thời gian cập nhật
}
