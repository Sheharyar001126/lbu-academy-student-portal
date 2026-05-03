package com.lbu.academy.repository;

import com.lbu.academy.entity.StudentProfile;
import com.lbu.academy.entity.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByPortalUser(PortalUser portalUser);
    Optional<StudentProfile> findByStudentId(String studentId);
}