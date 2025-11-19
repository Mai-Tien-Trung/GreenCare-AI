package com.example.GreenCareAI.repository;

import com.example.GreenCareAI.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    Optional<PaymentOrder> findByPayosOrderCode(Long payosOrderCode);
    boolean existsByPayosOrderCodeAndStatus(Long payosOrderCode, String status);
}