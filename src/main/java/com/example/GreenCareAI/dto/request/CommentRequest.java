package com.example.GreenCareAI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 2, max = 300, message = "Comment must be 2–300 characters")
    private String content;
}
