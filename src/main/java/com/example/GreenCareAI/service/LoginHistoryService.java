package com.example.GreenCareAI.service;

import com.example.GreenCareAI.entity.User;

import java.util.Map;

public interface LoginHistoryService {
    void saveLogin(User user);

    Map<String, Long> getLoginsPerDay();

    Long getLoginsLast30Days();
}
