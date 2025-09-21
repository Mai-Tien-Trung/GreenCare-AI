package com.example.GreenCareAI.controller;

import com.example.GreenCareAI.dto.request.ReminderRequest;
import com.example.GreenCareAI.dto.response.ReminderResponse;
import com.example.GreenCareAI.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    // Tạo reminder mới
    @PostMapping
    public ResponseEntity<ReminderResponse> create(
            @RequestBody ReminderRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        return ResponseEntity.ok(reminderService.create(request, username));
    }

    // Lấy tất cả reminder của user đang đăng nhập
    @GetMapping("/me")
    public ResponseEntity<List<ReminderResponse>> getMyReminders(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        return ResponseEntity.ok(reminderService.getByUsername(username));
    }

    // (Optional) Bật/tắt reminder
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ReminderResponse> toggle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        return ResponseEntity.ok(reminderService.toggle(id, username));
    }

    // Xoá reminder
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        reminderService.delete(id, username);
        return ResponseEntity.noContent().build();
    }
}
