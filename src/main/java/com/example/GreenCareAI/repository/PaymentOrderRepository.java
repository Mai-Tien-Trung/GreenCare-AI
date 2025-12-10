package com.example.GreenCareAI.repository;

import com.example.GreenCareAI.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    Optional<PaymentOrder> findByPayosOrderCode(Long payosOrderCode);

    boolean existsByPayosOrderCodeAndStatus(Long payosOrderCode, String status);

    // Revenue statistics queries
    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM PaymentOrder p WHERE p.status = :status")
    Double sumAmountByStatus(@Param("status") String status);

    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM PaymentOrder p WHERE p.status = :status AND p.processedAt >= :fromDate")
    Double sumAmountByStatusAndDateAfter(@Param("status") String status, @Param("fromDate") LocalDateTime fromDate);

    Long countByStatus(String status);

    @Query("SELECT COUNT(p) FROM PaymentOrder p WHERE p.status = :status AND p.processedAt >= :fromDate")
    Long countByStatusAndProcessedAtAfter(@Param("status") String status, @Param("fromDate") LocalDateTime fromDate);
}