package com.example.hospital.repository;

import com.example.hospital.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    Optional<Availability> findByDoctorIdAndDateAndTimeSlot(Long doctorId, LocalDate date, LocalTime timeSlot);
}
