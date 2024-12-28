package com.example.taskmanagementsystem.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequestDto {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Long authorId;
    private Long executorId;
    private String status;
    private String priority;
}
