package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentRequestDto;
import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.comment.CommentService;
import com.example.taskmanagementsystem.service.task.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/tasks")
public class UserTaskController {

    private final UserTaskService userTaskService;
    private final CommentService commentService;

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long id,
                                                            @RequestParam String status) {
        return ResponseEntity.ok(userTaskService.updateTaskStatus(id, status));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long id,
                                                         @RequestBody CommentRequestDto commentRequest) {
        return ResponseEntity.ok(commentService.addComment(id, commentRequest));
    }
}
