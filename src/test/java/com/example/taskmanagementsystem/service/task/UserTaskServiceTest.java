package com.example.taskmanagementsystem.service.task;


import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.mapper.TaskMapper;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class UserTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

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
    }

    @Test
    void updateTaskStatus_shouldReturnTaskResponseDto() {
        Long taskId = 1L;
        String status = "COMPLETED";

        Task task = mock(Task.class);
        User user = mock(User.class);
        TaskResponseDto responseDto = TaskResponseDto.builder()
                .id(taskId)
                .status(status)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(task.getExecutor()).thenReturn(user);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDto(task)).thenReturn(responseDto);

        TaskResponseDto result = userTaskService.updateTaskStatus(taskId, status);

        assertEquals(responseDto, result);
    }

    @Test
    void updateTaskStatus_shouldThrowEntityNotFoundException_whenTaskNotFound() {
        Long taskId = 1L;
        String status = "COMPLETED";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userTaskService.updateTaskStatus(taskId, status));
    }

    @Test
    void updateTaskStatus_shouldThrowAccessDeniedException_whenUserIsNotExecutor() {
        Long taskId = 1L;
        String status = "COMPLETED";

        Task task = mock(Task.class);
        User user = mock(User.class);
        User executor = mock(User.class);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(task.getExecutor()).thenReturn(executor);
        when(executor.getId()).thenReturn(2L);
        when(user.getId()).thenReturn(1L);

        assertThrows(AccessDeniedException.class, () -> userTaskService.updateTaskStatus(taskId, status));
    }
}
