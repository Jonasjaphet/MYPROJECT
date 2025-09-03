package com.example.hospital.dto.auth;

import com.example.hospital.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String name;

    @Email private String email;

    @Size(min = 6) private String password;

    private Role role = Role.PATIENT; // default role
}
