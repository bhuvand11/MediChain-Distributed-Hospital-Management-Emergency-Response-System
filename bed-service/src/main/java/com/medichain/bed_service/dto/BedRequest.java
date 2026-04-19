package com.medichain.bed_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BedRequest {

    @NotBlank(message = "Bed number is required")
    private String bedNumber;

    @NotBlank(message = "Ward type is required")
    private String wardType;

    @NotBlank(message = "Hospital name is required")
    private String hospitalName;

    private String status;
    private Long patientId;
    private String patientName;
}