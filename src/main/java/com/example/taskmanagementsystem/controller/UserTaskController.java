package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentRequestDto;
import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.comment.CommentService;
import com.example.taskmanagementsystem.service.task.UserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "User Task Management", description = "APIs for users to manage tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/tasks")
public class UserTaskController {

    private final UserTaskService userTaskService;
    private final CommentService commentService;

    @Operation(summary = "Update task status",
            description = "Allows executor to update task status by ID",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long id,
                                                            @RequestParam String status) {
        return ResponseEntity.ok(userTaskService.updateTaskStatus(id, status));
    }

    @Operation(summary = "Add comment to task",
            description = "Allows executor to add comments to a task",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long id,
                                                         @Valid @RequestBody CommentRequestDto commentRequest) {
        return ResponseEntity.ok(commentService.addComment(id, commentRequest));
    }
}
