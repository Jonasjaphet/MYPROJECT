package com.example.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDoctorRequest {
    @NotNull
    private Long userId;

    private String specialty;
    private String department;
}
