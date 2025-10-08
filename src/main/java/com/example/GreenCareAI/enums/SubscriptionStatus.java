package com.example.GreenCareAI.enums;



public enum SubscriptionStatus {
    PENDING,     // đăng ký nhưng chưa thanh toán
    AVAILABLE,   // gói sẵn sàng (đã mua, chưa active)
    ACTIVE,      // đang dùng
    COMPLETED,   // đã hết lượt hoặc hết hạn
    CANCELLED,   // bị hủy
    EXPIRED
}
