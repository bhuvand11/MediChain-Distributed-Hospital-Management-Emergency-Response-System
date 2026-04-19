package com.medichain.emergency_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import com.medichain.emergency_service.dto.BedAssignmentEvent;
import com.medichain.emergency_service.dto.DoctorAssignmentEvent;
import com.medichain.emergency_service.dto.EmergencyCreatedEvent;
import com.medichain.emergency_service.entity.Emergency;
import com.medichain.emergency_service.enums.EmergencyStatus;
import com.medichain.emergency_service.feign.BedServiceClient;
import com.medichain.emergency_service.feign.DoctorServiceClient;
import com.medichain.emergency_service.repository.EmergencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestrator {

    private final EmergencyRepository emergencyRepository;
    private final BedServiceWrapper bedServiceWrapper;
    private final DoctorServiceWrapper doctorServiceWrapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "emergency.created",
            groupId = "emergency-saga-group")
    public void handleEmergencyCreated(EmergencyCreatedEvent event) {
        log.info("Saga started for emergency ID: {}", event.getEmergencyId());

        Emergency emergency = emergencyRepository
                .findById(event.getEmergencyId())
                .orElse(null);

        if (emergency == null) {
            log.error("Emergency not found: {}", event.getEmergencyId());
            return;
        }

        boolean bedAssigned = false;
        boolean doctorAssigned = false;
        Long assignedBedId = null;
        Long assignedDoctorId = null;

        // Step 1 — Find and reserve nearest bed
        try {
            String wardType = getWardTypeForIncident(event.getIncidentType());
            String hospitalName = bedServiceWrapper
                    .findNearestHospitalWithBed(wardType);

            List<Object> availableBeds = (List<Object>) bedServiceWrapper
                    .getAvailableBedsByHospital(hospitalName);

            if (!availableBeds.isEmpty()) {
                Map<String, Object> bed = (Map<String, Object>) availableBeds.get(0);
                assignedBedId = Long.valueOf(bed.get("id").toString());

                bedServiceWrapper.updateBedStatus(
                        assignedBedId,
                        "OCCUPIED",
                        event.getPatientId(),
                        event.getPatientName()
                );

                emergency.setAssignedBedId(assignedBedId);
                emergency.setAssignedHospitalName(hospitalName);
                bedAssigned = true;

                log.info("Bed assigned: {} at {}", assignedBedId, hospitalName);

                // Publish bed updated event
                BedAssignmentEvent bedEvent = BedAssignmentEvent.builder()
                        .emergencyId(event.getEmergencyId())
                        .bedId(assignedBedId)
                        .hospitalName(hospitalName)
                        .wardType(wardType)
                        .success(true)
                        .build();
                kafkaTemplate.send("bed.updated", bedEvent);
            }
        } catch (Exception e) {
            log.error("Bed assignment failed for emergency {}: {}",
                    event.getEmergencyId(), e.getMessage());
        }

        // Step 2 — Assign available doctor
        try {
            List<Object> availableDoctors = doctorServiceWrapper.getAvailableDoctors();

            if (!availableDoctors.isEmpty()) {
                Map<String, Object> doctor =
                        (Map<String, Object>) availableDoctors.get(0);
                assignedDoctorId = Long.valueOf(doctor.get("id").toString());
                String doctorName = doctor.get("fullName").toString();

                doctorServiceWrapper.toggleDoctorAvailability(assignedDoctorId);

                emergency.setAssignedDoctorId(assignedDoctorId);
                emergency.setAssignedDoctorName(doctorName);
                doctorAssigned = true;

                log.info("Doctor assigned: {} - {}", assignedDoctorId, doctorName);

                DoctorAssignmentEvent doctorEvent = DoctorAssignmentEvent.builder()
                        .emergencyId(event.getEmergencyId())
                        .doctorId(assignedDoctorId)
                        .doctorName(doctorName)
                        .success(true)
                        .build();
                kafkaTemplate.send("notification.sent", doctorEvent);
            }
        } catch (Exception e) {
            log.error("Doctor assignment failed for emergency {}: {}",
                    event.getEmergencyId(), e.getMessage());
        }

        // Step 3 — Assign ambulance
        String ambulanceId = "AMB-" + UUID.randomUUID().toString()
                .substring(0, 6).toUpperCase();
        emergency.setAmbulanceId(ambulanceId);
        emergency.setEstimatedArrivalTime("8-12 minutes");

        // Saga decision — update status based on results
        if (bedAssigned && doctorAssigned) {
            emergency.setStatus(EmergencyStatus.DISPATCHED);
            log.info("Emergency {} fully dispatched!", event.getEmergencyId());
        } else if (bedAssigned || doctorAssigned) {
            emergency.setStatus(EmergencyStatus.DISPATCHED);
            log.warn("Emergency {} partially dispatched", event.getEmergencyId());
        } else {
            // Compensating transaction — rollback bed if doctor failed
            if (bedAssigned && assignedBedId != null) {
                try {
                    bedServiceWrapper.updateBedStatus(
                            assignedBedId, "AVAILABLE", null, null);
                    log.info("Compensating transaction: bed {} released",
                            assignedBedId);
                } catch (Exception e) {
                    log.error("Compensating transaction failed: {}", e.getMessage());
                }
            }
            emergency.setStatus(EmergencyStatus.FAILED);
            log.error("Emergency {} failed — compensating transactions executed",
                    event.getEmergencyId());
        }

        emergencyRepository.save(emergency);
    }

    private String getWardTypeForIncident(String incidentType) {
        return switch (incidentType.toUpperCase()) {
            case "CARDIAC", "STROKE", "TRAUMA" -> "ICU";
            case "PEDIATRIC" -> "PEDIATRIC";
            case "MATERNITY" -> "MATERNITY";
            default -> "GENERAL";
        };
    }
    private String bedServiceFallback(Exception e) {
        log.error("Bed service circuit breaker opened: {}", e.getMessage());
        return null;
    }

    private String doctorServiceFallback(Exception e) {
        log.error("Doctor service circuit breaker opened: {}", e.getMessage());
        return null;
    }
}