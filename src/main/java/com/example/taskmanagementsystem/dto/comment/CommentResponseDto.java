package com.example.taskmanagementsystem.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private Long taskId;
    private String content;
}
