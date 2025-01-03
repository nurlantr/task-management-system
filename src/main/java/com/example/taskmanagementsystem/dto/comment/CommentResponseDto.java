package com.example.taskmanagementsystem.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Response object for comments")
@Data
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    @Schema(description = "Comment ID", example = "1")
    private Long id;

    @Schema(description = "User ID of the commenter", example = "5")
    private Long userId;

    @Schema(description = "Task ID the comment belongs to", example = "10")
    private Long taskId;

    @Schema(description = "Content of the comment", example = "This is a sample comment")
    private String content;
}
