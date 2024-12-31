package com.example.taskmanagementsystem.service.comment;

import com.example.taskmanagementsystem.dto.comment.CommentRequestDto;
import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.mapper.CommentMapper;
import com.example.taskmanagementsystem.repository.CommentRepository;
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

class CommentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

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
    void addComment_shouldReturnCommentResponseDto() {
        Long taskId = 1L;
        CommentRequestDto commentRequest = new CommentRequestDto("Test comment");

        Task task = mock(Task.class);
        User user = mock(User.class);
        Comment comment = mock(Comment.class);
        CommentResponseDto responseDto = new CommentResponseDto(1L, 1L, taskId, "Test comment", "2024-12-31");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(task.getExecutor()).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentResponseDto(comment)).thenReturn(responseDto);

        CommentResponseDto result = commentService.addComment(taskId, commentRequest);

        assertEquals(responseDto, result);
    }

    @Test
    void addComment_shouldThrowEntityNotFoundException_whenTaskNotFound() {
        Long taskId = 1L;
        CommentRequestDto commentRequest = new CommentRequestDto("Test comment");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.addComment(taskId, commentRequest));
    }

    @Test
    void addComment_shouldThrowAccessDeniedException_whenUserIsNotExecutor() {
        Long taskId = 1L;
        CommentRequestDto commentRequest = new CommentRequestDto("Test comment");

        Task task = mock(Task.class);
        User user = mock(User.class);
        User executor = mock(User.class);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(task.getExecutor()).thenReturn(executor);
        when(executor.getId()).thenReturn(2L);
        when(user.getId()).thenReturn(1L);

        assertThrows(AccessDeniedException.class, () -> commentService.addComment(taskId, commentRequest));
    }
}