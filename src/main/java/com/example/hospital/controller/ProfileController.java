package com.example.hospital.controller;

import com.example.hospital.dto.CreateDoctorRequest;
import com.example.hospital.dto.CreatePatientRequest;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Doctor> createDoctor(@RequestBody CreateDoctorRequest req) {
        return ResponseEntity.ok(profileService.createDoctor(req));
    }

    @PostMapping("/patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<Patient> createPatient(@RequestBody CreatePatientRequest req) {
        return ResponseEntity.ok(profileService.createPatient(req));
    }
}
