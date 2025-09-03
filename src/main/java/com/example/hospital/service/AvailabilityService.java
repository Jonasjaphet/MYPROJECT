package com.example.hospital.service;

import com.example.hospital.dto.AvailabilityDTO;
import com.example.hospital.model.Availability;
import com.example.hospital.model.Doctor;
import com.example.hospital.repository.AvailabilityRepository;
import com.example.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    public Availability addSlot(AvailabilityDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        LocalDate d = LocalDate.parse(dto.getDate());
        LocalTime t = LocalTime.parse(dto.getTimeSlot());

        availabilityRepository.findByDoctorIdAndDateAndTimeSlot(doctor.getId(), d, t)
                .ifPresent(a -> { throw new RuntimeException("Slot already exists"); });

        Availability av = Availability.builder().doctor(doctor).date(d).timeSlot(t).build();
        return availabilityRepository.save(av);
    }

    public List<Availability> getSlots(Long doctorId, String date) {
        LocalDate d = LocalDate.parse(date);
        return availabilityRepository.findByDoctorIdAndDate(doctorId, d);
    }
}
