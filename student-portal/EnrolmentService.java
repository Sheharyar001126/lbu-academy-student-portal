package com.lbu.academy.service;

import com.lbu.academy.entity.*;
import com.lbu.academy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CourseRepository courseRepository;
    private final PortalUserRepository portalUserRepository;
    private final AccountService accountService;

    public Enrolment enrol(String username, Long courseId) {
        PortalUser user = portalUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        StudentProfile profile = studentProfileRepository.findByPortalUser(user)
                .orElseGet(() -> createStudentProfile(user));

        if (enrolmentRepository.existsByStudentProfileAndCourseId(profile, courseId)) {
            throw new RuntimeException("Already enrolled in this course");
        }

        accountService.createInvoice(profile.getStudentId(), course.getFee());

        Enrolment enrolment = new Enrolment();
        enrolment.setStudentProfile(profile);
        enrolment.setCourse(course);
        return enrolmentRepository.save(enrolment);
    }

    public List<Enrolment> getEnrolments(String username) {
        PortalUser user = portalUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StudentProfile profile = studentProfileRepository.findByPortalUser(user)
                .orElseThrow(() -> new RuntimeException("No student profile found"));
        return enrolmentRepository.findByStudentProfile(profile);
    }

    private StudentProfile createStudentProfile(PortalUser user) {
        StudentProfile profile = new StudentProfile();
        profile.setStudentId("STU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        profile.setPortalUser(user);
        profile.setFirstName("");
        profile.setLastName("");
        StudentProfile saved = studentProfileRepository.save(profile);
        accountService.createFinanceAccount(saved.getStudentId());
        accountService.createLibraryAccount(saved.getStudentId());
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return saved;
    }
}