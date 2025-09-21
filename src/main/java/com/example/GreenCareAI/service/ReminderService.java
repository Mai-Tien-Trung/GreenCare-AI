package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.request.ReminderRequest;
import com.example.GreenCareAI.dto.response.ReminderResponse;

import java.util.List;

public interface ReminderService {
    ReminderResponse create(ReminderRequest request, String username);
    List<ReminderResponse> getByUsername(String username);
    ReminderResponse toggle(Long id, String username);
    void delete(Long id, String username);
}

