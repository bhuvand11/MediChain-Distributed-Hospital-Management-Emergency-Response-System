package com.medichain.doctor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String department;
    private String hospitalName;
    private String licenseNumber;
    private Integer experienceYears;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
}
