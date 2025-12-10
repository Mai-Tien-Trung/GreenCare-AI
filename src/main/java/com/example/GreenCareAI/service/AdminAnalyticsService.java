package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.response.RevenueStatsResponse;
import com.example.GreenCareAI.dto.response.SubscriptionStatsResponse;
import com.example.GreenCareAI.dto.response.UserStatsResponse;

public interface AdminAnalyticsService {
    RevenueStatsResponse getRevenueStats();

    UserStatsResponse getUserStats();

    SubscriptionStatsResponse getSubscriptionStats();
}
