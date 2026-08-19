package com.aldokenfack.SickleCareAI.repository;

import com.aldokenfack.SickleCareAI.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Admin> findByPublicId(UUID publicId);

}
