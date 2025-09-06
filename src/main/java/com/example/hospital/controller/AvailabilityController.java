package com.example.hospital.controller;

import com.example.hospital.dto.AvailabilityDTO;
import com.example.hospital.model.Availability;
import com.example.hospital.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    // 🩺 Only Doctors/Admins can add slots
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Availability> addSlot(@RequestBody AvailabilityDTO dto) {
        return ResponseEntity.ok(availabilityService.addSlot(dto));
    }

    // 👥 Public: Patients can view slots of a doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Availability>> getSlots(@PathVariable Long doctorId,
                                                       @RequestParam String date) {
        return ResponseEntity.ok(availabilityService.getSlots(doctorId, date));
    }
}

