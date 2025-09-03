package com.example.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentDTO {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private String date; // format: yyyy-MM-dd

    @NotNull
    private String time; // format: HH:mm
}
