package com.medichain.emergency_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmergencyRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Patient phone is required")
    private String patientPhone;

    @NotBlank(message = "Incident type is required")
    private String incidentType;

    @NotBlank(message = "Location is required")
    private String location;

    private Double latitude;
    private Double longitude;
    private String notes;
}