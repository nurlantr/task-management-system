package com.example.taskmanagementsystem.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request object for creating a new comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @Schema(description = "Content of the comment", example = "This is a sample comment")
    @NotBlank(message = "Content must not be blank")
    private String content;
}
