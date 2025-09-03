package com.example.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvailabilityDTO {
    @NotNull
    private Long doctorId;

    @NotNull
    private String date; // format: yyyy-MM-dd

    @NotNull
    private String timeSlot; // format: HH:mm
}
