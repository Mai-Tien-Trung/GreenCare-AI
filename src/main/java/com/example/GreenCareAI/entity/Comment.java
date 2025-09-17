package com.example.GreenCareAI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;   // ai comment

    @ManyToOne
    @JoinColumn(name = "post_id")

    private Post post;   // comment thuộc post nào

    private LocalDateTime createdAt = LocalDateTime.now();
}
