package com.example.GreenCareAI.dto.response;

import com.example.GreenCareAI.enums.Frequency;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class ReminderResponse {
    private Long id;
    private String title;
    private LocalTime time;
    private Frequency frequency;
    private List<DayOfWeek> daysOfWeek;
    private boolean active;
    private Long userId;
}
