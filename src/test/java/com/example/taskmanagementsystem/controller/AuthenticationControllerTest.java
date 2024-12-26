package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.auth.AuthenticationController;
import com.example.taskmanagementsystem.auth.AuthenticationRequest;
import com.example.taskmanagementsystem.auth.AuthenticationResponse;
import com.example.taskmanagementsystem.auth.AuthenticationService;
import com.example.taskmanagementsystem.auth.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();

        registerRequest = RegisterRequest.builder()
                .firstname("Naruto")
                .lastname("Uzumaki")
                .email("naruto@gmail.com")
                .password("password")
                .build();
    }

    @Test
    void testRegister() throws Exception {
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("mockJwtToken")
                .build();

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockJwtToken"));
    }

    @Test
    void testAuthenticate() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest("naruto@gmail.com", "password");
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("mockJwtToken")
                .build();

        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockJwtToken"));
    }
}
