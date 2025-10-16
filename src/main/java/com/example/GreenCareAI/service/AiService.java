package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.request.PromptRequest;

public interface AiService {
    String getChatResponse(PromptRequest promptRequest);
}