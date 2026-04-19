package com.medichain.emergency_service.dto;

import com.medichain.emergency_service.enums.EmergencyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private String patientPhone;
    private String incidentType;
    private String location;
    private Double latitude;
    private Double longitude;
    private EmergencyStatus status;
    private Long assignedDoctorId;
    private String assignedDoctorName;
    private Long assignedBedId;
    private String assignedHospitalName;
    private String ambulanceId;
    private String estimatedArrivalTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}