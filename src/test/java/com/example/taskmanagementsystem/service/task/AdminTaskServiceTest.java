package com.example.taskmanagementsystem.service.task;

import com.example.taskmanagementsystem.dto.task.TaskRequestDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.mapper.TaskMapper;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private AdminTaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_shouldReturnTaskResponseDto() {
        TaskRequestDto request = new TaskRequestDto();
        User author = new User();
        Task savedTask = new Task();
        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build();

        when(userRepository.findById(request.getAuthorId())).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toTaskResponseDto(savedTask)).thenReturn(response);

        TaskResponseDto result = taskService.createTask(request);

        assertEquals(response, result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnUpdatedTaskResponseDto() {
        Long id = 1L;
        TaskRequestDto request = new TaskRequestDto();
        Task task = new Task();
        Task updatedTask = new Task();
        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        when(taskMapper.toTaskResponseDto(updatedTask)).thenReturn(response);

        TaskResponseDto result = taskService.updateTask(id, request);

        assertEquals(response, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks_shouldReturnListOfTaskResponseDto() {
        List<Task> tasks = List.of(new Task());
        List<TaskResponseDto> response = List.of(TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build());

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toTaskResponseDto(any(Task.class))).thenReturn(response.get(0));

        List<TaskResponseDto> result = taskService.getAllTasks();

        assertEquals(response, result);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void deleteTask_shouldInvokeRepositoryDelete() {
        Long id = 1L;

        taskService.deleteTask(id);

        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    void assignTaskExecutor_shouldReturnUpdatedTaskResponseDto() {
        Long id = 1L;
        Long executorId = 2L;
        Task task = new Task();
        User executor = new User();
        Task updatedTask = new Task();
        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Task Title")
                .description("Task Description")
                .status("Pending")
                .priority("High")
                .authorName("Author Name")
                .executorName("Executor Name")
                .build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(userRepository.findById(executorId)).thenReturn(Optional.of(executor));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        when(taskMapper.toTaskResponseDto(updatedTask)).thenReturn(response);

        TaskResponseDto result = taskService.assignTaskExecutor(id, executorId);

        assertEquals(response, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getFilteredTasks_shouldReturnFilteredTasks() {
        Long authorId = 1L;
        String status = "Pending";
        String priority = "High";
        int page = 0, size = 10;

        List<Task> tasks = List.of(new Task(), new Task());
        Page<Task> taskPage = new PageImpl<>(tasks);
        List<TaskResponseDto> response = List.of(
                TaskResponseDto.builder()
                        .id(1L)
                        .title("Task Title 1")
                        .status(status)
                        .priority(priority)
                        .build(),
                TaskResponseDto.builder()
                        .id(2L)
                        .title("Task Title 2")
                        .status("Completed")
                        .priority("Low")
                        .build()
        );

        when(taskRepository.findByAuthorIdWithFilters(authorId, status, priority, PageRequest.of(page, size)))
                .thenReturn(taskPage);
        when(taskMapper.toTaskResponseDto(any(Task.class))).thenReturn(response.get(0), response.get(1));

        List<TaskResponseDto> result = taskService.getFilteredTasks(authorId, null, status, priority, page, size);

        assertEquals(response, result);
        verify(taskRepository, times(1)).findByAuthorIdWithFilters(authorId, status, priority, PageRequest.of(page, size));
    }

}
