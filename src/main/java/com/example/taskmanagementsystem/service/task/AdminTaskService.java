package com.example.taskmanagementsystem.service.task;

import com.example.taskmanagementsystem.dto.task.TaskRequestDto;
import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.mapper.TaskMapper;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDto createTask(TaskRequestDto taskRequest) {
        User author = userRepository.findById(taskRequest.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        User executor = taskRequest.getExecutorId() != null
                ? userRepository.findById(taskRequest.getExecutorId()).orElse(null)
                : null;
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .priority(taskRequest.getPriority())
                .author(author)
                .executor(executor)
                .build();
        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDto(savedTask);
    }


    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User executor = taskRequest.getExecutorId() != null
                ? userRepository.findById(taskRequest.getExecutorId()).orElse(null)
                : null;

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setPriority(taskRequest.getPriority());
        task.setExecutor(executor);

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDto(updatedTask);
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskResponseDto assignTaskExecutor(Long id, Long executorId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new EntityNotFoundException("Executor not found"));
        task.setExecutor(executor);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDto(updatedTask);
    }

    public List<TaskResponseDto> getFilteredTasks(Long authorId, Long executorId, String status,
                                                  String priority, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Task> tasksPage;
        if (authorId != null) {
            tasksPage = taskRepository.findByAuthorIdWithFilters(authorId, status, priority, pageable);
        } else if (executorId != null) {
            tasksPage = taskRepository.findByExecutorIdWithFilters(executorId, status, priority, pageable);
        } else {
            throw new IllegalArgumentException("Either authorId or executorId must be provided");
        }
        return tasksPage.stream()
                .map(taskMapper::toTaskResponseDto)
                .toList();

    }
}
