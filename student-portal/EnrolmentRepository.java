package com.lbu.academy.repository;

import com.lbu.academy.entity.Enrolment;
import com.lbu.academy.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    List<Enrolment> findByStudentProfile(StudentProfile studentProfile);
    boolean existsByStudentProfileAndCourseId(StudentProfile studentProfile, Long courseId);
}