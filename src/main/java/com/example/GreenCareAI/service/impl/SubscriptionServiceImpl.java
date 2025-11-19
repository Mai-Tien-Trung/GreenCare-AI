package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.dto.response.PaymentStatusResponse;
import com.example.GreenCareAI.entity.PaymentOrder;
import com.example.GreenCareAI.entity.Subscription;
import com.example.GreenCareAI.entity.SubscriptionPlan;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.enums.SubscriptionStatus;
import com.example.GreenCareAI.repository.PaymentOrderRepository;
import com.example.GreenCareAI.repository.SubscriptionPlanRepository;
import com.example.GreenCareAI.repository.SubscriptionRepository;
import com.example.GreenCareAI.repository.UserRepository;
import com.example.GreenCareAI.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;

// KHẮC PHỤC: SỬ DỤNG CÁC LỚP V2 (THEO GỢI Ý CỦA IDE VÀ LỖI BIÊN DỊCH)
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserRepository userRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PayOS payOS; // Inject PayOS SDK

    // KHẮC PHỤC LỖI V2: Bỏ @Value("${payos.webhook.url}")
    // Webhook URL phải được cấu hình trên Dashboard PayOS, không gửi qua API nữa.
    @Value("${payos.return.url}")
    private String returnUrl;
    @Value("${payos.cancel.url}")
    private String cancelUrl;

    @Override
    public Subscription createFreeSubscriptionByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SubscriptionPlan freePlan = planRepository.findByName("Free")
                .orElseThrow(() -> new RuntimeException("Free plan not found"));

        Subscription sub = Subscription.builder()
                .user(user)
                .plan(freePlan)
                .remainingScans(freePlan.getMaxScans())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(freePlan.getDurationDays()))
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

        if (LocalDate.now().isAfter(sub.getEndDate())) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(sub);
            throw new RuntimeException("Your subscription has expired. Please renew or upgrade.");
        }

        if (sub.getPlan().getMaxScans() == null) {
            return sub; // Unlimited scans
        }

        if (sub.getRemainingScans() <= 0) {
            sub.setStatus(SubscriptionStatus.COMPLETED);
            subscriptionRepository.save(sub);
            throw new RuntimeException("You have used all available scans for this plan.");
        }

        sub.setRemainingScans(sub.getRemainingScans() - 1);

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

    @Scheduled(cron = "0 0 0 * * ?")
    public void expireOldSubscriptions() {
        LocalDate today = LocalDate.now();
        var activeSubs = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        for (Subscription sub : activeSubs) {
            if (today.isAfter(sub.getEndDate())) {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(sub);
                System.out.println("🔔 Subscription expired for user: " + sub.getUser().getUsername());
            }
        }
    }

    // =========================================================
    // LOGIC TÍCH HỢP PAYOS (CẬP NHẬT V2 BUILDER)
    // =========================================================

    @Override
    public Map<String, String> createPayOSOrder(String username, String planName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SubscriptionPlan plan = planRepository.findByName(planName)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        long orderCodeLong = System.currentTimeMillis() % 100000000000L + user.getId();

        PaymentOrder order = PaymentOrder.builder()
                .payosOrderCode(orderCodeLong) // Lưu Long vào DB
                .user(user)
                .plan(plan)
                .amount(plan.getPrice())
                .status("PENDING")
                .build();

        paymentOrderRepository.save(order);

        // KHẮC PHỤC: SỬ DỤNG BUILDER V2 VÀ ĐÚNG KIỂU DỮ LIỆU
        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("NC " + plan.getName())
                .quantity(1) // (Integer)
                .price((long) plan.getPrice()) // (Long)
                .build();

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCodeLong) // (Long)
                .amount((long) plan.getPrice()) // (Long)
                .description("TT " + plan.getName() + " #" + user.getId())
                .items(List.of(item))
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                // KHÔNG CÓ .webhookUrl(webhookUrl) - Phải cấu hình trên Dashboard PayOS
                .build();

        try {
            // KHẮC PHỤC LỖI 1: Thay 'client' bằng 'payOS' (biến đã inject)
            // KHẮC PHỤC LỖI 3 (Nếu có): Phương thức V2 là 'paymentRequests().create()'
            CreatePaymentLinkResponse response = payOS.paymentRequests().create(paymentData);

            return Map.of(
                    // KHẮC PHỤC LỖI 2: Thay 'payosResponse' bằng 'response' (biến đã định nghĩa)
                    "paymentLink", response.getCheckoutUrl(),
                    "orderCode", String.valueOf(orderCodeLong)
            );
        } catch (Exception e) {
            paymentOrderRepository.delete(order);
            throw new RuntimeException("Lỗi tạo link thanh toán PayOS: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public void handleSuccessfulPayment(Long payosOrderCode) {
        PaymentOrder order = paymentOrderRepository.findByPayosOrderCode(payosOrderCode)
                .orElseThrow(() -> new RuntimeException("PaymentOrder not found with code: " + payosOrderCode));

        if ("SUCCESS".equals(order.getStatus())) {
            System.out.println("Payment order " + payosOrderCode + " already processed.");
            return;
        }

        User user = order.getUser();
        SubscriptionPlan plan = order.getPlan();

        subscriptionRepository.findByUserIdAndStatus(user.getId(), SubscriptionStatus.ACTIVE)
                .ifPresent(old -> {
                    old.setStatus(SubscriptionStatus.COMPLETED);
                    subscriptionRepository.save(old);
                });

        Subscription newSub = Subscription.builder()
                .user(user)
                .plan(plan)
                .remainingScans(plan.getMaxScans())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(plan.getDurationDays()))
                .status(SubscriptionStatus.ACTIVE)
                .build();

        subscriptionRepository.save(newSub);

        order.setStatus("SUCCESS");
        order.setProcessedAt(LocalDateTime.now());
        paymentOrderRepository.save(order);

        System.out.println("✅ Nâng cấp gói thành công cho user: " + user.getUsername() + " lên gói: " + plan.getName());
    }

    @Override
    public PaymentStatusResponse getPaymentStatus(Long orderCode, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PaymentOrder order = paymentOrderRepository.findByPayosOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied: Order does not belong to user");
        }

        String status = order.getStatus();
        String message = switch (status) {
            case "SUCCESS" -> "Thanh toán thành công. Gói đã được nâng cấp.";
            case "PENDING" -> "Đang chờ thanh toán/xác nhận từ ngân hàng.";
            case "CANCELED", "EXPIRED", "FAILED" -> "Đơn hàng đã bị hủy hoặc thất bại.";
            default -> "Trạng thái không xác định.";
        };

        return new PaymentStatusResponse(orderCode, status, message);
    }
}