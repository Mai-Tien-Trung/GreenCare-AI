package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.entity.Subscription;
import com.example.GreenCareAI.entity.SubscriptionPlan;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.enums.SubscriptionStatus;
import com.example.GreenCareAI.repository.SubscriptionPlanRepository;
import com.example.GreenCareAI.repository.SubscriptionRepository;
import com.example.GreenCareAI.repository.UserRepository;
import com.example.GreenCareAI.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
@Transactional
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public Subscription createFreeSubscriptionByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SubscriptionPlan freePlan = planRepository.findByName("Free")
                .orElseThrow(() -> new RuntimeException("Free plan not found"));

        Subscription sub = Subscription.builder()
                .user(user)
                .plan(freePlan)
                .remainingScans(freePlan.getMaxScans()) // ví dụ: 5
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(freePlan.getDurationDays())) // ví dụ: 7 ngày
                .status(SubscriptionStatus.ACTIVE)
                .build();

        return subscriptionRepository.save(sub);
    }

    @Override
    public Subscription upgradeToPremiumByUsername(String username, String planName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SubscriptionPlan plan = planRepository.findByName(planName)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Đánh dấu gói cũ là COMPLETED
        subscriptionRepository.findByUserIdAndStatus(user.getId(), SubscriptionStatus.ACTIVE)
                .ifPresent(old -> {
                    old.setStatus(SubscriptionStatus.COMPLETED);
                    subscriptionRepository.save(old);
                });

        Subscription sub = Subscription.builder()
                .user(user)
                .plan(plan)
                .remainingScans(plan.getMaxScans()) // null = unlimited
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(plan.getDurationDays()))
                .status(SubscriptionStatus.ACTIVE)
                .build();

        return subscriptionRepository.save(sub);
    }

    @Override
    public Subscription deductScanByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription sub = subscriptionRepository
                .findByUserIdAndStatus(user.getId(), SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription"));

        //  1. Kiểm tra hết hạn
        if (LocalDate.now().isAfter(sub.getEndDate())) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(sub);
            throw new RuntimeException("Your subscription has expired. Please renew or upgrade.");
        }

        //  2. Nếu là Premium (Unlimited scans)
        if (sub.getPlan().getMaxScans() == null) {
            return sub; // Không trừ lượt, vẫn active
        }

        //  3. Nếu là gói có giới hạn lượt
        if (sub.getRemainingScans() <= 0) {
            sub.setStatus(SubscriptionStatus.COMPLETED);
            subscriptionRepository.save(sub);
            throw new RuntimeException("You have used all available scans for this plan.");
        }

        //  4. Trừ lượt
        sub.setRemainingScans(sub.getRemainingScans() - 1);

        // 5. Nếu vừa hết lượt sau khi trừ
        if (sub.getRemainingScans() == 0) {
            sub.setStatus(SubscriptionStatus.COMPLETED);
        }

        return subscriptionRepository.save(sub);
    }


    @Override
    public Subscription getActiveSubscriptionByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionRepository.findByUserIdAndStatus(user.getId(), SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription"));
    }
    @Scheduled(cron = "0 0 0 * * ?") // chạy mỗi ngày lúc 00:00
    public void expireOldSubscriptions() {
        LocalDate today = LocalDate.now();

        // Lấy toàn bộ sub đang active
        var activeSubs = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        for (Subscription sub : activeSubs) {
            if (today.isAfter(sub.getEndDate())) {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(sub);
                System.out.println("🔔 Subscription expired for user: " + sub.getUser().getUsername());
            }
        }
        }
}
