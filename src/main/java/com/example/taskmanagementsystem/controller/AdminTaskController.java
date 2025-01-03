package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.task.TaskRequestDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.task.AdminTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Task Management", description = "APIs for admins to manage tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tasks")
public class AdminTaskController {
    private final AdminTaskService taskService;

    @Operation(summary = "Create a new task",
               description = "Allows admin to create a new task",
               security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/create")
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequest) {
        TaskResponseDto response = taskService.createTask(taskRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a task",
              description = "Allows admin to update a task by ID",
               security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id,
                                                     @Valid @RequestBody TaskRequestDto taskRequest) {
        TaskResponseDto response = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Assign task",
            description = "Allows admin to assign task executor",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDto> assignExecutor(@PathVariable Long id, @RequestParam Long executorId) {
        TaskResponseDto response = taskService.assignTaskExecutor(id, executorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a task",
            description = "Allows admin to delete a task by ID",
            security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task successfully deleted!");
    }
}
