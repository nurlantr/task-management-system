package com.example.taskmanagementsystem.service.comment;

import com.example.taskmanagementsystem.dto.comment.CommentRequestDto;
import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.ResourceNotFoundException;
import com.example.taskmanagementsystem.exception.TaskAccessDeniedException;
import com.example.taskmanagementsystem.mapper.CommentMapper;
import com.example.taskmanagementsystem.repository.CommentRepository;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
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

    @Mock
    private SecurityUtil securityUtil;

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

        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        when(securityUtil.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void addComment_shouldReturnCommentResponseDto() {
        Long taskId = 1L;
        CommentRequestDto commentRequest = new CommentRequestDto("Test comment");

        Task task = mock(Task.class);
        User user = mock(User.class);
        Comment comment = mock(Comment.class);
        CommentResponseDto responseDto = new CommentResponseDto(1L, 1L, taskId, "Test comment");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(task.getExecutor()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
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

        assertThrows(ResourceNotFoundException.class, () -> commentService.addComment(taskId, commentRequest));
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

        assertThrows(TaskAccessDeniedException.class, () -> commentService.addComment(taskId, commentRequest));
    }

    @Test
    void getCommentsForTask_shouldReturnCommentsForAdmin() {
        Long taskId = 1L;
        Task task = new Task();
        User admin = new User();
        admin.setRole(Role.ROLE_ADMIN);
        List<Comment> comments = List.of(new Comment(), new Comment());
        List<CommentResponseDto> response = List.of(
                new CommentResponseDto(1L, 1L, taskId, "Comment 1"),
                new CommentResponseDto(2L, 2L, taskId, "Comment 2")
        );

        when(securityUtil.getCurrentUser()).thenReturn(admin);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(admin));
        when(commentRepository.findAllByTaskId(taskId)).thenReturn(comments);
        when(commentMapper.toCommentResponseDto(any(Comment.class))).thenReturn(response.get(0), response.get(1));

        List<CommentResponseDto> result = commentService.getCommentsForTask(taskId);

        assertEquals(response, result);
    }

    @Test
    void getCommentsForTask_shouldThrowAccessDeniedException() {
        Long taskId = 1L;
        Task task = new Task();
        User user = new User();
        user.setRole(Role.ROLE_USER);
        task.setExecutor(new User());
        task.getExecutor().setId(2L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        assertThrows(TaskAccessDeniedException.class, () -> commentService.getCommentsForTask(taskId));
    }

}