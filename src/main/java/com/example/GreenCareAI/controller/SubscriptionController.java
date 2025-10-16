package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.entity.Subscription;
import com.example.GreenCareAI.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    //  Nâng cấp Premium
    @PostMapping("/upgrade")
    public ResponseEntity<Subscription> upgradeToPremium(
            @RequestParam String planName,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.upgradeToPremiumByUsername(userDetails.getUsername(), planName));
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
