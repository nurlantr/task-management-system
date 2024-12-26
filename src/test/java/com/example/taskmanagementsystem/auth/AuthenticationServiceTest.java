package com.example.taskmanagementsystem.auth;

import com.example.taskmanagementsystem.config.JwtService;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("Aigul")
                .lastname("Arman")
                .email("aigul@gmail.com")
                .password("password")
                .build();

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("mockJwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("mockJwtToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest("aigul@gmail.com", "password");

        User user = User.builder()
                .firstname("Aigul")
                .lastname("Arman")
                .email("aigul@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("mockJwtToken", response.getToken());
        verify(authenticationManager, times(1))
                .authenticate(any());
        verify(userRepository, times(1)).findByEmail(request.getEmail());
    }
}
