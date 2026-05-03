package com.lbu.academy.controller;

import com.lbu.academy.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/graduation")
@RequiredArgsConstructor
public class GraduationController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> checkGraduation(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(profileService.checkGraduation(userDetails.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}