package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.task.TaskRequestDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.task.AdminTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminTaskControllerTest {

    @Mock
    private AdminTaskService taskService;

    @InjectMocks
    private AdminTaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_shouldReturnTaskResponseDto() {
        TaskRequestDto request = new TaskRequestDto();
        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build();

        when(taskService.createTask(request)).thenReturn(response);

        ResponseEntity<TaskResponseDto> result = taskController.createTask(request);

        assertEquals(response, result.getBody());
        verify(taskService, times(1)).createTask(request);
    }

    @Test
    void updateTask_shouldReturnUpdatedTaskResponseDto() {
        Long id = 1L;
        TaskRequestDto request = new TaskRequestDto();
        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build();

        when(taskService.updateTask(id, request)).thenReturn(response);

        ResponseEntity<TaskResponseDto> result = taskController.updateTask(id, request);

        assertEquals(response, result.getBody());
        verify(taskService, times(1)).updateTask(id, request);
    }

    @Test
    void assignExecutor_shouldReturnUpdatedTaskResponseDto() {
        Long id = 1L;
        Long executorId = 2L;
        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build();

        when(taskService.assignTaskExecutor(id, executorId)).thenReturn(response);

        ResponseEntity<TaskResponseDto> result = taskController.assignExecutor(id, executorId);

        assertEquals(response, result.getBody());
        verify(taskService, times(1)).assignTaskExecutor(id, executorId);
    }

    @Test
    void deleteTask_shouldReturnSuccessMessage() {
        Long id = 1L;

        ResponseEntity<String> result = taskController.deleteTask(id);

        assertEquals("Task successfully deleted!", result.getBody());
        verify(taskService, times(1)).deleteTask(id);
    }
}
