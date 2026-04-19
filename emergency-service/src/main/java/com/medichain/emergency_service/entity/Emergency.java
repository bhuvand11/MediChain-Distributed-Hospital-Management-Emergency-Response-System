package com.medichain.emergency_service.entity;

import com.medichain.emergency_service.enums.EmergencyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergencies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String patientPhone;

    @Column(nullable = false)
    private String incidentType;

    @Column(nullable = false)
    private String location;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmergencyStatus status;

    private Long assignedDoctorId;
    private String assignedDoctorName;

    private Long assignedBedId;
    private String assignedHospitalName;

    private String ambulanceId;
    private String estimatedArrivalTime;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = EmergencyStatus.CREATED;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}