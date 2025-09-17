package com.example.GreenCareAI.repository;

import com.example.GreenCareAI.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    // Thống kê số lượt login theo ngày
    @Query("SELECT DATE(l.loginAt) as date, COUNT(l) " +
            "FROM LoginHistory l " +
            "GROUP BY DATE(l.loginAt) " +
            "ORDER BY DATE(l.loginAt)")
    List<Object[]> countLoginsPerDay();

    @Query("SELECT COUNT(l) FROM LoginHistory l WHERE l.loginAt >= :fromDate")
    Long countLoginsLast30Days(LocalDateTime fromDate);
}
