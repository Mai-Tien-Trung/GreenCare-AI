package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.dto.response.PaymentStatusResponse; // 🆕 Import DTO
import com.example.GreenCareAI.entity.Subscription;
import com.example.GreenCareAI.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // 🆕 Import Map

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // Xem gói hiện tại
    @GetMapping("/me")
    public ResponseEntity<Subscription> getMySubscription(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptionByUsername(userDetails.getUsername()));
    }

    // 🆕 Tạo link thanh toán PayOS (Thay thế cho /upgrade)
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(
            @RequestParam String planName,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Gọi Service để tạo đơn hàng PayOS và trả về link
        Map<String, String> result = subscriptionService.createPayOSOrder(
                userDetails.getUsername(),
                planName
        );

        // Trả về {paymentLink, orderCode} cho App
        return ResponseEntity.ok(result);
    }

    // 🆕 Endpoint Polling: App sẽ gọi để kiểm tra trạng thái thanh toán
    @GetMapping("/payment/status")
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(
            @RequestParam Long orderCode,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(subscriptionService.getPaymentStatus(orderCode, userDetails.getUsername()));
    }


    // Trừ lượt khi detect AI
    @PutMapping("/deduct")
    public ResponseEntity<Subscription> deductScan(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.deductScanByUsername(userDetails.getUsername()));
    }

    //  Reset Free plan (debug hoặc user reset)
    @PutMapping("/reset-free")
    public ResponseEntity<Subscription> resetFree(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.createFreeSubscriptionByUsername(userDetails.getUsername()));
    }
}