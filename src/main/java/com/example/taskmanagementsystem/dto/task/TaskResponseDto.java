package com.example.taskmanagementsystem.dto.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String authorName;
    private String executorName;
}
