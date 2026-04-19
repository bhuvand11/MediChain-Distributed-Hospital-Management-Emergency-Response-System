package com.medichain.emergency_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAssignmentEvent {
    private Long emergencyId;
    private Long doctorId;
    private String doctorName;
    private boolean success;
    private String failureReason;
}