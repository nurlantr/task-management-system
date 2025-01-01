package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    private List<CommentResponseDto> comments;
}
