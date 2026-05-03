package com.lbu.academy.repository;

import com.lbu.academy.entity.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PortalUserRepository extends JpaRepository<PortalUser, Long> {
    Optional<PortalUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}