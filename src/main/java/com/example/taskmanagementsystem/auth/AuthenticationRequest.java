package com.example.taskmanagementsystem.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request object for user authentication")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @Schema(description = "Email of the user", example = "user@example.com")
    private String email;

    @Schema(description = "Password of the user", example = "password123")
    String password;
}
