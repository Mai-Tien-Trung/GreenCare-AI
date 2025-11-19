package com.example.GreenCareAI.dto.response;

public record PaymentStatusResponse(Long orderCode, String status, String message) {}