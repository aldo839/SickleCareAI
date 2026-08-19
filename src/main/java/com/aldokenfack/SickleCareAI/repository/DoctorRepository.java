package com.aldokenfack.SickleCareAI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aldokenfack.SickleCareAI.model.Doctor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Doctor> findByPublicId(UUID publicId);

}
