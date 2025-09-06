package com.example.hospital.service;

import com.example.hospital.dto.CreateDoctorRequest;
import com.example.hospital.dto.CreatePatientRequest;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;
import com.example.hospital.model.Role;   // make sure you have this enum
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public Doctor createDoctor(CreateDoctorRequest r) {
        User user = userRepository.findById(r.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // validate role
        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User with ID " + r.getUserId() + " is not registered as DOCTOR");
        }

        // validate if doctor profile already exists
        if (doctorRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Doctor profile already exists for this user");
        }

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialty(r.getSpecialty())
                .department(r.getDepartment())
                .build();

        return doctorRepository.save(doctor);
    }

    public Patient createPatient(CreatePatientRequest r) {
        User user = userRepository.findById(r.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // validate role
        if (user.getRole() != Role.PATIENT) {
            throw new RuntimeException("User with ID " + r.getUserId() + " is not registered as PATIENT");
        }

        // validate if patient profile already exists
        if (patientRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Patient profile already exists for this user");
        }

        Patient patient = Patient.builder()
                .user(user)
                .dob(r.getDob())
                .phone(r.getPhone())
                .address(r.getAddress())
                .build();

        return patientRepository.save(patient);
    }
}
