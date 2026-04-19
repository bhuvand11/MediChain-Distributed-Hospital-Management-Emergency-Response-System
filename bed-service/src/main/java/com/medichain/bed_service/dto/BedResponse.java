package com.medichain.bed_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BedResponse {
    private Long id;
    private String bedNumber;
    private String wardType;
    private String hospitalName;
    private String status;
    private Long patientId;
    private String patientName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}