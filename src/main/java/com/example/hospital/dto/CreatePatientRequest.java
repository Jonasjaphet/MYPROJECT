package com.example.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePatientRequest {
    @NotNull
    private Long userId;

    private String dob;
    private String phone;
    private String address;
}
