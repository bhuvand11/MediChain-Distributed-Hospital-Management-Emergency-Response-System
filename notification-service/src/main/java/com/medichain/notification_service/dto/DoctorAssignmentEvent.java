package com.medichain.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAssignmentEvent {
    private Long emergencyId;
    private Long doctorId;
    private String doctorName;
    private boolean success;
    private String failureReason;
}