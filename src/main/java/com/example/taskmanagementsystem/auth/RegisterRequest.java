package com.example.taskmanagementsystem.auth;

import com.example.taskmanagementsystem.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request object for user registration")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Schema(description = "First name of the user", example = "Naruto")
    private String firstname;

    @Schema(description = "Last name of the user", example = "Uzumaki")
    private String lastname;

    @Schema(description = "Email of the user", example = "naruto@example.com")
    private String email;

    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @Schema(description = "Role of the user", example = "ROLE_USER", defaultValue = "ROLE_USER")
    private Role role = Role.ROLE_USER;
}
