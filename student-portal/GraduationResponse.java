package com.lbu.academy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GraduationResponse {
    private String studentId;
    private boolean eligibleToGraduate;
    private String message;
}