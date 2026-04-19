package com.medichain.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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