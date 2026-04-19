package com.medichain.pharmacy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {
    private Long id;
    private String name;
    private String manufacturer;
    private String category;
    private Integer stockQuantity;
    private Double pricePerUnit;
    private String description;
    private String sideEffects;
    private String storageConditions;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
}