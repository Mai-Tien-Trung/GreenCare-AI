package com.example.GreenCareAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã đơn hàng được tạo và gửi cho PayOS (orderCode), cần unique
    @Column(unique = true, nullable = false)
    private Long payosOrderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    // Số tiền (lưu bằng double để đối chiếu với Plan)
    @Column(nullable = false)
    private double amount;

    // Trạng thái: PENDING, SUCCESS, CANCELED
    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime processedAt;
}