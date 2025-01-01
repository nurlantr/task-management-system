package com.example.taskmanagementsystem.service.comment;

import com.example.taskmanagementsystem.dto.comment.CommentRequestDto;
import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.mapper.CommentMapper;
import com.example.taskmanagementsystem.repository.CommentRepository;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentResponseDto addComment(Long taskId, CommentRequestDto commentRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User currentUser = getCurrentUser();
        if (task.getExecutor().getId() != currentUser.getId()) {
            throw new AccessDeniedException("You can only comment on tasks assigned to you");
        }

        Comment comment = Comment.builder()
                .task(task)
                .content(commentRequest.getContent())
                .user(currentUser)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentResponseDto(savedComment);
    }

    public List<CommentResponseDto> getCommentsForTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            return commentRepository.findAllByTaskId(id).stream()
                    .map(commentMapper::toCommentResponseDto)
                    .toList();
        }

        if (currentUser.getRole() == Role.ROLE_USER && task.getExecutor() != null
                && task.getExecutor().getId() == getCurrentUser().getId()
        ) {
            return commentRepository.findAllByTaskId(id).stream()
                    .map(commentMapper::toCommentResponseDto)
                    .toList();
        }
        throw new AccessDeniedException("You do not have permission to view comments on this task");
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
