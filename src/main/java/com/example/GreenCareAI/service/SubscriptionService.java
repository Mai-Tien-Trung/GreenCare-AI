package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.response.PaymentStatusResponse;
import com.example.GreenCareAI.entity.Subscription;

import java.util.Map;

public interface SubscriptionService {



    // 🔄 Trừ lượt khi dùng AI (nếu là Free)
    Subscription deductScanByUsername(String username);

    // 👀 Lấy thông tin gói hiện tại
    Subscription getActiveSubscriptionByUsername(String username);

    // 🆕 Tạo đơn hàng PayOS và trả về URL thanh toán
    Map<String, String> createPayOSOrder(String username, String planName);

    // 🆕 Xử lý thanh toán thành công từ Webhook (logic nâng cấp gói)
    void handleSuccessfulPayment(Long payosOrderCode);

    // 🆕 Lấy trạng thái thanh toán của một đơn hàng (dùng cho Polling)
    PaymentStatusResponse getPaymentStatus(Long orderCode, String username);
    Subscription createFreeSubscriptionByUsername(String username);
}