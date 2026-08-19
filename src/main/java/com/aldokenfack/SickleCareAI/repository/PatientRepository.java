package com.aldokenfack.SickleCareAI.repository;

import com.aldokenfack.SickleCareAI.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Patient> findByPublicId(UUID publicId);

    Optional<Patient> findByDoctorId(Long doctorId);

}
