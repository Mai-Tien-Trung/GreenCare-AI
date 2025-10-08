package com.example.GreenCareAI.service;

import com.example.GreenCareAI.entity.Subscription;

public interface SubscriptionService {

    // 🆓 Tạo gói free cho user mới (dựa theo username từ token)
    Subscription createFreeSubscriptionByUsername(String username);

    // 💎 Nâng cấp lên Premium (Monthly / Yearly)
    Subscription upgradeToPremiumByUsername(String username, String planName);

    // 🔄 Trừ lượt khi dùng AI (nếu là Free)
    Subscription deductScanByUsername(String username);

    // 👀 Lấy thông tin gói hiện tại
    Subscription getActiveSubscriptionByUsername(String username);
}
