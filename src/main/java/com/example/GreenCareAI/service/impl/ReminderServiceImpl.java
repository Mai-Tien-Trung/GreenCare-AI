package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.dto.request.ReminderRequest;
import com.example.GreenCareAI.dto.response.ReminderResponse;
import com.example.GreenCareAI.entity.Reminder;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.repository.ReminderRepository;
import com.example.GreenCareAI.repository.UserRepository;
import com.example.GreenCareAI.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    @Override
    public ReminderResponse create(ReminderRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reminder reminder = new Reminder();
        reminder.setTitle(request.getTitle());
        reminder.setTime(request.getTime());
        reminder.setFrequency(request.getFrequency());
        reminder.setDaysOfWeek(request.getDaysOfWeek());
        reminder.setUser(user);
        reminder.setActive(true);

        return mapToResponse(reminderRepository.save(reminder));
    }

    @Override
    public List<ReminderResponse> getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reminderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReminderResponse toggle(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied");
        }

        reminder.setActive(!reminder.isActive());
        return mapToResponse(reminderRepository.save(reminder));
    }

    @Override
    public void delete(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permission denied");
        }

        reminderRepository.delete(reminder);
    }

    private ReminderResponse mapToResponse(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .time(reminder.getTime())
                .frequency(reminder.getFrequency())
                .daysOfWeek(reminder.getDaysOfWeek())
                .active(reminder.isActive())
                .userId(reminder.getUser().getId())
                .build();
    }
}
