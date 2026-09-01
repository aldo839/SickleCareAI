package com.aldokenfack.SickleCareAI.repository;

import com.aldokenfack.SickleCareAI.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByCode(String code);

    void delete(Validation validation);
}
