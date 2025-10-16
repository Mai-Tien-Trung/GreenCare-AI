package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.dto.request.ChatGPTRequest;
import com.example.GreenCareAI.dto.request.PromptRequest;
import com.example.GreenCareAI.dto.response.ChatGPTResponse;
import com.example.GreenCareAI.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    private final RestClient restClient;

    public AiServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Value("${openapi.api.key}")
    private String apiKey;

    @Value("${openapi.api.model}")
    private String model;

    public String getChatResponse(PromptRequest promptRequest){

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(
                model,
                List.of(new ChatGPTRequest.Message("user", promptRequest.prompt()))
        );

        ChatGPTResponse response = restClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(chatGPTRequest)
                .retrieve()
                .body(ChatGPTResponse.class);

        return response.choices().get(0).message().content();

    }
}