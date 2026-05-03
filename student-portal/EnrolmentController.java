package com.lbu.academy.controller;

import com.lbu.academy.service.EnrolmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/enrolments")
@RequiredArgsConstructor
public class EnrolmentController {

    private final EnrolmentService enrolmentService;

    @PostMapping
    public ResponseEntity<?> enrol(@RequestBody Map<String, Long> body,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(enrolmentService.enrol(userDetails.getUsername(), body.get("courseId")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping
    public ResponseEntity<?> getEnrolments(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(enrolmentService.getEnrolments(userDetails.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}