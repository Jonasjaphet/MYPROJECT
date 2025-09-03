package com.example.hospital.service;

import com.example.hospital.dto.CreateDoctorRequest;
import com.example.hospital.dto.CreatePatientRequest;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.User;
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
        Doctor doctor = Doctor.builder()
                .user(user).specialty(r.getSpecialty()).department(r.getDepartment())
                .build();
        return doctorRepository.save(doctor);
    }

    public Patient createPatient(CreatePatientRequest r) {
        User user = userRepository.findById(r.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = Patient.builder()
                .user(user).dob(r.getDob()).phone(r.getPhone()).address(r.getAddress())
                .build();
        return patientRepository.save(patient);
    }
}
