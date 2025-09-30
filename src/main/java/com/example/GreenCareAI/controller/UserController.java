package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.dto.request.ProfileUpdateRequest;
import com.example.GreenCareAI.dto.response.UserResponse;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal(expression = "id") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody ProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
