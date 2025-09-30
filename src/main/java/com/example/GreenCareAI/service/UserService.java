package com.example.GreenCareAI.service;

import com.example.GreenCareAI.dto.request.ProfileUpdateRequest;
import com.example.GreenCareAI.dto.response.UserResponse;
import com.example.GreenCareAI.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    User updateProfile(Long id, ProfileUpdateRequest request);
    List<UserResponse> getAllUsers();
    void deleteUser(Long id);          // cho admin
}
