package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentRequestDto;
import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.comment.CommentService;
import com.example.taskmanagementsystem.service.task.UserTaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserTaskControllerTest {

    @Mock
    private UserTaskService userTaskService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private UserTaskController userTaskController;

    UserTaskControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateTaskStatus_shouldReturnUpdatedTaskResponseDto() {
        Long taskId = 1L;
        String status = "IN_PROGRESS";

        TaskResponseDto taskResponseDto = TaskResponseDto.builder()
                .id(taskId)
                .status(status)
                .build();

        when(userTaskService.updateTaskStatus(eq(taskId), eq(status))).thenReturn(taskResponseDto);

        ResponseEntity<TaskResponseDto> response = userTaskController.updateTaskStatus(taskId, status);

        assertEquals(taskResponseDto, response.getBody());
    }

    @Test
    void updateTaskStatus_shouldThrowAccessDeniedException() {
        Long taskId = 1L;
        String status = "IN_PROGRESS";

        when(userTaskService.updateTaskStatus(eq(taskId), eq(status))).thenThrow(new org.springframework.security.access.AccessDeniedException("You can only update tasks assigned to you"));

        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> userTaskController.updateTaskStatus(taskId, status));
    }

    @Test
    void addComment_shouldReturnCommentResponseDto() {
        Long taskId = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto("Sample content");
        CommentResponseDto commentResponseDto = new CommentResponseDto(1L, 1L, taskId, "Sample content");

        when(commentService.addComment(eq(taskId), any(CommentRequestDto.class))).thenReturn(commentResponseDto);

        ResponseEntity<CommentResponseDto> response = userTaskController.addComment(taskId, commentRequestDto);

        assertEquals(commentResponseDto, response.getBody());
    }

    @Test
    void addComment_shouldThrowAccessDeniedException() {
        Long taskId = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto("Sample content");

        when(commentService.addComment(eq(taskId), any(CommentRequestDto.class))).thenThrow(new org.springframework.security.access.AccessDeniedException("You can only comment on tasks assigned to you"));

        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> userTaskController.addComment(taskId, commentRequestDto));
    }
}