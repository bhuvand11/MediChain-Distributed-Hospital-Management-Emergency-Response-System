package com.medichain.emergency_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCreatedEvent {
    private Long emergencyId;
    private Long patientId;
    private String patientName;
    private String patientPhone;
    private String incidentType;
    private String location;
    private Double latitude;
    private Double longitude;
    private String wardType;
}