package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "Response object for tasks")
@Data
@Builder
public class TaskResponseDto {
    @Schema(description = "Task ID", example = "1")
    private Long id;

    @Schema(description = "Title of the task", example = "Implement API")
    private String title;

    @Schema(description = "Description of the task", example = "Implement Swagger documentation for the project")
    private String description;

    @Schema(description = "Status of the task", example = "completed")
    private String status;

    @Schema(description = "Priority of the task", example = "high")
    private String priority;

    @Schema(description = "Name of the author", example = "John Doe")
    private String authorName;

    @Schema(description = "Name of the executor", example = "Jane Smith")
    private String executorName;

    @Schema(description = "List of comments associated with the task")
    private List<CommentResponseDto> comments;
}
