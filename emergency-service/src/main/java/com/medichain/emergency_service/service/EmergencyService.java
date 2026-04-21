package com.medichain.emergency_service.service;
//emergency service

import com.medichain.emergency_service.dto.EmergencyCreatedEvent;
import com.medichain.emergency_service.dto.EmergencyRequest;
import com.medichain.emergency_service.dto.EmergencyResponse;
import com.medichain.emergency_service.entity.Emergency;
import com.medichain.emergency_service.enums.EmergencyStatus;
import com.medichain.emergency_service.exception.ResourceNotFoundException;
import com.medichain.emergency_service.repository.EmergencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EmergencyResponse triggerSOS(EmergencyRequest request) {
        // Save emergency to DB first
        Emergency emergency = Emergency.builder()
                .patientId(request.getPatientId())
                .patientName(request.getPatientName())
                .patientPhone(request.getPatientPhone())
                .incidentType(request.getIncidentType())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .notes(request.getNotes())
                .build();

        Emergency saved = emergencyRepository.save(emergency);
        log.info("Emergency created with ID: {}", saved.getId());

        // Publish event to Kafka — Saga begins
        EmergencyCreatedEvent event = EmergencyCreatedEvent.builder()
                .emergencyId(saved.getId())
                .patientId(saved.getPatientId())
                .patientName(saved.getPatientName())
                .patientPhone(saved.getPatientPhone())
                .incidentType(saved.getIncidentType())
                .location(saved.getLocation())
                .latitude(saved.getLatitude())
                .longitude(saved.getLongitude())
                .wardType(getWardType(saved.getIncidentType()))
                .build();

        kafkaTemplate.send("emergency.created", event);
        log.info("Emergency event published to Kafka for ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    public EmergencyResponse getEmergencyById(Long id) {
        Emergency emergency = emergencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Emergency not found with id: " + id));
        return mapToResponse(emergency);
    }

    public List<EmergencyResponse> getAllEmergencies() {
        return emergencyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EmergencyResponse> getEmergenciesByPatient(Long patientId) {
        return emergencyRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EmergencyResponse> getEmergenciesByStatus(EmergencyStatus status) {
        return emergencyRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmergencyResponse updateStatus(Long id, EmergencyStatus status) {
        Emergency emergency = emergencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Emergency not found with id: " + id));
        emergency.setStatus(status);
        if (status == EmergencyStatus.RESOLVED) {
            emergency.setResolvedAt(LocalDateTime.now());
        }
        return mapToResponse(emergencyRepository.save(emergency));
    }

    private String getWardType(String incidentType) {
        return switch (incidentType.toUpperCase()) {
            case "CARDIAC", "STROKE", "TRAUMA" -> "ICU";
            case "PEDIATRIC" -> "PEDIATRIC";
            case "MATERNITY" -> "MATERNITY";
            default -> "GENERAL";
        };
    }

    private EmergencyResponse mapToResponse(Emergency emergency) {
        return EmergencyResponse.builder()
                .id(emergency.getId())
                .patientId(emergency.getPatientId())
                .patientName(emergency.getPatientName())
                .patientPhone(emergency.getPatientPhone())
                .incidentType(emergency.getIncidentType())
                .location(emergency.getLocation())
                .latitude(emergency.getLatitude())
                .longitude(emergency.getLongitude())
                .status(emergency.getStatus())
                .assignedDoctorId(emergency.getAssignedDoctorId())
                .assignedDoctorName(emergency.getAssignedDoctorName())
                .assignedBedId(emergency.getAssignedBedId())
                .assignedHospitalName(emergency.getAssignedHospitalName())
                .ambulanceId(emergency.getAmbulanceId())
                .estimatedArrivalTime(emergency.getEstimatedArrivalTime())
                .notes(emergency.getNotes())
                .createdAt(emergency.getCreatedAt())
                .updatedAt(emergency.getUpdatedAt())
                .build();
    }
}
