package com.medichain.patient_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalHistoryRequest {
    private Long id;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String treatment;
    private String medications;
    private String doctorName;
    private String hospitalName;

    @NotNull(message = "Diagnosis date is required")
    private LocalDate diagnosisDate;

    private LocalDate recoveryDate;
    private String notes;
}