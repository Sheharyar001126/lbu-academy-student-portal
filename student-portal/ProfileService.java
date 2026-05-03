package com.lbu.academy.service;

import com.lbu.academy.dto.request.ProfileUpdateRequest;
import com.lbu.academy.dto.response.GraduationResponse;
import com.lbu.academy.dto.response.ProfileResponse;
import com.lbu.academy.entity.PortalUser;
import com.lbu.academy.entity.StudentProfile;
import com.lbu.academy.repository.PortalUserRepository;
import com.lbu.academy.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final PortalUserRepository portalUserRepository;
    private final AccountService accountService;

    public ProfileResponse getProfile(String username) {
        PortalUser user = portalUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StudentProfile profile = studentProfileRepository.findByPortalUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return new ProfileResponse(
                profile.getStudentId(),
                profile.getFirstName(),
                profile.getLastName(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public ProfileResponse updateProfile(String username, ProfileUpdateRequest request) {
        PortalUser user = portalUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StudentProfile profile = studentProfileRepository.findByPortalUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        studentProfileRepository.save(profile);
        return new ProfileResponse(
                profile.getStudentId(),
                profile.getFirstName(),
                profile.getLastName(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public GraduationResponse checkGraduation(String username) {
        PortalUser user = portalUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StudentProfile profile = studentProfileRepository.findByPortalUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        boolean hasBalance = accountService.hasOutstandingBalance(profile.getStudentId());
        boolean eligible = !hasBalance;
        String message = eligible
                ? "Congratulations! You are eligible to graduate."
                : "You have outstanding invoices. Please clear them before graduation.";
        return new GraduationResponse(profile.getStudentId(), eligible, message);
    }
}