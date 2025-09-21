package com.example.GreenCareAI.entity;


import com.example.GreenCareAI.enums.Frequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @ElementCollection
    private List<DayOfWeek> daysOfWeek;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

