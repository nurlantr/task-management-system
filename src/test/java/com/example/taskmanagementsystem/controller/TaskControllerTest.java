package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.service.comment.CommentService;
import com.example.taskmanagementsystem.service.task.AdminTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskControllerTest {

    @Mock
    private AdminTaskService taskService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_shouldReturnListOfTaskResponseDto() {
        List<TaskResponseDto> response = List.of(TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build());

        when(taskService.getAllTasks()).thenReturn(response);

        ResponseEntity<List<TaskResponseDto>> result = taskController.getAllTasks();

        assertEquals(response, result.getBody());
        verify(taskService, times(1)).getAllTasks();
    }


    @Test
    void getTasksWithFilters_shouldReturnFilteredTasks() {
        List<TaskResponseDto> tasks = List.of(
                TaskResponseDto.builder()
                        .id(1L)
                        .title("Task Title 1")
                        .description("Description 1")
                        .status("Pending")
                        .priority("High")
                        .authorName("Author 1")
                        .executorName("Executor 1")
                        .build(),
                TaskResponseDto.builder()
                        .id(2L)
                        .title("Task Title 2")
                        .description("Description 2")
                        .status("Completed")
                        .priority("Low")
                        .authorName("Author 2")
                        .executorName("Executor 2")
                        .build()
        );

        when(taskService.getFilteredTasks(null, null, null, null, 0, 10)).thenReturn(tasks);

        ResponseEntity<List<TaskResponseDto>> result = taskController.getTasksWithFilters(null, null, null, null, 0, 10);

        assertEquals(tasks, result.getBody());
        verify(taskService, times(1)).getFilteredTasks(null, null, null, null, 0, 10);
    }

    @Test
    void getCommentsForTask_shouldReturnComments() {
        Long taskId = 1L;
        List<CommentResponseDto> comments = List.of(
                new CommentResponseDto(1L, 1L, taskId, "Comment 1"),
                new CommentResponseDto(2L, 2L, taskId, "Comment 2")
        );

        when(commentService.getCommentsForTask(taskId)).thenReturn(comments);

        ResponseEntity<List<CommentResponseDto>> result = taskController.getCommentsForTask(taskId);

        assertEquals(comments, result.getBody());
        verify(commentService, times(1)).getCommentsForTask(taskId);
    }

}
