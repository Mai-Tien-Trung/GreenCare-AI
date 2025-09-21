package com.example.GreenCareAI.dto.request;

import com.example.GreenCareAI.enums.Frequency;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
public class ReminderRequest {
    private String title;
    private LocalTime time;
    private Frequency frequency;
    private List<DayOfWeek> daysOfWeek;
    private Long userId;
}