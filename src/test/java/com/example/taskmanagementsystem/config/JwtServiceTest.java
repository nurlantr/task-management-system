package com.example.taskmanagementsystem.config;

import com.example.taskmanagementsystem.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateAndValidateToken() {
        var user = User.builder()
                .firstname("Minato")
                .lastname("Namikaze")
                .email("minato@gmail.com")
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, user));
        assertEquals("minato@gmail.com", jwtService.extractUsername(token));
    }
}
