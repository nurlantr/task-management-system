package com.example.taskmanagementsystem.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Request object for creating or updating tasks")
@Data
public class TaskRequestDto {
    @Schema(description = "Title of the task", example = "Implement API")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Description of the task", example = "Implement Swagger documentation for the project")
    private String description;

    @Schema(description = "Author ID of the task", example = "1")
    @NotNull(message = "Author ID cannot be null")
    private Long authorId;

    @Schema(description = "Executor ID of the task", example = "2")
    private Long executorId;

    @Schema(description = "Status of the task", example = "pending")
    private String status;

    @Schema(description = "Priority of the task", example = "high")
    private String priority;
}
