package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.comment.CommentService;
import com.example.taskmanagementsystem.service.task.AdminTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Task Management", description = "APIs for both admins and users to manage tasks")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final AdminTaskService taskService;
    private final CommentService commentService;

    @Operation(summary = "Get all tasks",
            description = "Retrieve all tasks",
            security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks with filters",
            description = "Retrieve all tasks with optional filtering and pagination",
            security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasksWithFilters(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long executorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page index must not be negative and size must be greater than zero");
        }
        List<TaskResponseDto> tasks = taskService.getFilteredTasks(authorId, executorId, status, priority, page, size);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get comments for task", description = "Retrieve all comments for task")
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForTask(@PathVariable Long id) {
        List<CommentResponseDto> comments = commentService.getCommentsForTask(id);
        return ResponseEntity.ok(comments);
    }
}
