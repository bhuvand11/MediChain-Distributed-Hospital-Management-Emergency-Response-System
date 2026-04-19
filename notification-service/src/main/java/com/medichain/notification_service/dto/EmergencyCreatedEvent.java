package com.medichain.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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