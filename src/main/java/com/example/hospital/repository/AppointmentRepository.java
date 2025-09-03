package com.example.hospital.repository;

import com.example.hospital.model.Appointment;
import com.example.hospital.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    Optional<Appointment> findByDoctorIdAndDateAndTime(Long doctorId, LocalDate date, LocalTime time);
    Optional<Appointment> findByPatientIdAndDateAndTime(Long patientId, LocalDate date, LocalTime time);
    long countByDoctorIdAndDateAndTimeAndStatus(Long doctorId, LocalDate date, LocalTime time, AppointmentStatus status);
}
