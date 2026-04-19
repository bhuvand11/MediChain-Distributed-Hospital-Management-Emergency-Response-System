package com.medichain.emergency_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BedAssignmentEvent {
    private Long emergencyId;
    private Long bedId;
    private String hospitalName;
    private String wardType;
    private boolean success;
    private String failureReason;
}