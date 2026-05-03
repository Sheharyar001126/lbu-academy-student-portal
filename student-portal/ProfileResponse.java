package com.lbu.academy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private String studentId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}