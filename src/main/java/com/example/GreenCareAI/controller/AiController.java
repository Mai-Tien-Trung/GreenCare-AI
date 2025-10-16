package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.dto.request.PromptRequest;
import com.example.GreenCareAI.service.AiService;
import org.springframework.web.bind.annotation.*;



@CrossOrigin("*")
@RestController
@RequestMapping("/api/chat")
public class AiController {

    private final AiService chatGPTService;

    public AiController(AiService chatGPTService){
        this.chatGPTService = chatGPTService;
    }

    @PostMapping
    public String chat(@RequestBody PromptRequest promptRequest){
        return chatGPTService.getChatResponse(promptRequest);
    }
}
