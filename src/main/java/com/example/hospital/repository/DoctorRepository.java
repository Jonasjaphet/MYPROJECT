package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
}
