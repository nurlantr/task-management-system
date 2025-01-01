package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.dto.task.TaskRequestDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.comment.CommentService;
import com.example.taskmanagementsystem.service.task.AdminTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class AdminTaskController {
    private final AdminTaskService taskService;
    private final CommentService commentService;

    @PostMapping("/admin/create")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id,
                                                    @Valid @RequestBody TaskRequestDto taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/admin/{id}/assign")
    public ResponseEntity<TaskResponseDto> assignExecutor(@PathVariable Long id, @RequestParam Long executorId) {
        return ResponseEntity.ok(taskService.assignTaskExecutor(id, executorId));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task successfully deleted!");
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasksWithFilters(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long executorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<TaskResponseDto> tasks = taskService.getFilteredTasks(authorId, executorId, status, priority, page, size);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForTask(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsForTask(id));
    }

}
