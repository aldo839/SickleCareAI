package com.aldokenfack.SickleCareAI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aldokenfack.SickleCareAI.model.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
