package com.aldokenfack.SickleCareAI.repository;

import com.aldokenfack.SickleCareAI.model.Role;
import com.aldokenfack.SickleCareAI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByRole(Role role);

    Optional<User> findByPublicId(UUID publicId);

}
