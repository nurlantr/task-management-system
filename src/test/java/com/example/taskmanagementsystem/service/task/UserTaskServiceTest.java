package com.example.taskmanagementsystem.service.task;


import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.ResourceNotFoundException;
import com.example.taskmanagementsystem.exception.TaskAccessDeniedException;
import com.example.taskmanagementsystem.mapper.TaskMapper;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class UserTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private UserTaskService userTaskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User mockUser = new User();
        mockUser.setId(1L);
        when(securityUtil.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void updateTaskStatus_shouldReturnTaskResponseDto() {
        Long taskId = 1L;
        String status = "COMPLETED";

        Task task = new Task();
        User currentUser = new User();
        currentUser.setId(1L);
        task.setExecutor(currentUser);

        TaskResponseDto responseDto = TaskResponseDto.builder()
                .id(taskId)
                .status(status)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDto(task)).thenReturn(responseDto);

        TaskResponseDto result = userTaskService.updateTaskStatus(taskId, status);

        assertEquals(responseDto, result);
    }

    @Test
    void updateTaskStatus_shouldThrowResourceNotFoundException_whenTaskNotFound() {
        Long taskId = 1L;
        String status = "COMPLETED";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userTaskService.updateTaskStatus(taskId, status)
        );

        assertEquals("Task not found with ID: " + taskId, exception.getMessage());
    }

    @Test
    void updateTaskStatus_shouldThrowTaskAccessDeniedException_whenUserIsNotExecutor() {
        Long taskId = 1L;
        String status = "COMPLETED";

        Task task = new Task();
        User currentUser = new User();
        currentUser.setId(1L);

        User executor = new User();
        executor.setId(2L);

        task.setExecutor(executor);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(securityUtil.getCurrentUser()).thenReturn(currentUser);

        TaskAccessDeniedException exception = assertThrows(
                TaskAccessDeniedException.class,
                () -> userTaskService.updateTaskStatus(taskId, status)
        );

        assertEquals("You can only update tasks assigned to you.", exception.getMessage());
    }
}
