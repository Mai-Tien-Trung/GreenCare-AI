package com.example.GreenCareAI.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription_plan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // "Free", "Premium-Monthly", "Premium-Yearly"
    private Integer maxScans; // null hoặc 0 nghĩa là unlimited
    private int durationDays;
    private double price;
}
