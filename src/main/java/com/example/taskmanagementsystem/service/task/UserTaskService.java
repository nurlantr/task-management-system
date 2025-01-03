package com.example.taskmanagementsystem.service.task;

import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.ResourceNotFoundException;
import com.example.taskmanagementsystem.exception.TaskAccessDeniedException;
import com.example.taskmanagementsystem.mapper.TaskMapper;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserTaskService {
    private final TaskRepository taskRepository;
    private final SecurityUtil securityUtil;
    private final TaskMapper taskMapper;


    public TaskResponseDto updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        User currentUser = securityUtil.getCurrentUser();

        if (task.getExecutor() == null
                || task.getExecutor().getId() != currentUser.getId()) {
            throw new TaskAccessDeniedException("You can only update tasks assigned to you.");
        }

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDto(updatedTask);
    }

}
