package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.request.LoginRequest;
import com.example.GreenCareAI.dto.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterRequest request);
    ResponseEntity<?> login(LoginRequest request);
}
