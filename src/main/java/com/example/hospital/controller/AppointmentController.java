package com.example.hospital.controller;

import com.example.hospital.dto.AppointmentDTO;
import com.example.hospital.model.Appointment;
import com.example.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<Appointment> book(@RequestBody AppointmentDTO dto) {
        return ResponseEntity.ok(appointmentService.book(dto));
    }

    @GetMapping("/patient/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getByPatient(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.byPatient(id));
    }

    @GetMapping("/doctor/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getByDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.byDoctor(id));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Appointment> approve(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.approve(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Appointment> reject(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.reject(id));
    }
}
