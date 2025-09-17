package com.example.GreenCareAI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")

    private User user;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
