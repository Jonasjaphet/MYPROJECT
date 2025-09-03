package com.example.hospital.service;

import com.example.hospital.dto.AppointmentDTO;
import com.example.hospital.model.*;
import com.example.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilityRepository availabilityRepository;

    @Transactional
    public Appointment book(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        LocalDate date = LocalDate.parse(dto.getDate());
        LocalTime time = LocalTime.parse(dto.getTime());

        // Ensure slot exists
        availabilityRepository.findByDoctorIdAndDateAndTimeSlot(doctor.getId(), date, time)
                .orElseThrow(() -> new RuntimeException("Doctor not available at this time"));

        // Prevent double booking
        appointmentRepository.findByDoctorIdAndDateAndTime(doctor.getId(), date, time)
                .ifPresent(a -> { throw new RuntimeException("Slot already booked"); });
        appointmentRepository.findByPatientIdAndDateAndTime(patient.getId(), date, time)
                .ifPresent(a -> { throw new RuntimeException("Patient already has an appointment at this time"); });

        Appointment ap = Appointment.builder()
                .patient(patient).doctor(doctor).date(date).time(time).status(AppointmentStatus.PENDING)
                .build();
        return appointmentRepository.save(ap);
    }

    public List<Appointment> byPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> byDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Transactional
    public Appointment approve(Long appointmentId) {
        Appointment ap = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        ap.setStatus(AppointmentStatus.APPROVED);
        return appointmentRepository.save(ap);
    }

    @Transactional
    public Appointment reject(Long appointmentId) {
        Appointment ap = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        ap.setStatus(AppointmentStatus.REJECTED);
        return appointmentRepository.save(ap);
    }
}
