package com.medichain.emergency_service.controller;

import com.medichain.emergency_service.dto.EmergencyRequest;
import com.medichain.emergency_service.dto.EmergencyResponse;
import com.medichain.emergency_service.enums.EmergencyStatus;
import com.medichain.emergency_service.service.EmergencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/emergency")
@RequiredArgsConstructor
public class EmergencyController {

    private final EmergencyService emergencyService;

    @PostMapping("/sos")
    public ResponseEntity<EmergencyResponse> triggerSOS(
            @Valid @RequestBody EmergencyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emergencyService.triggerSOS(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyResponse> getEmergencyById(
            @PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.getEmergencyById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmergencyResponse>> getAllEmergencies() {
        return ResponseEntity.ok(emergencyService.getAllEmergencies());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmergencyResponse>> getEmergenciesByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                emergencyService.getEmergenciesByPatient(patientId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmergencyResponse>> getEmergenciesByStatus(
            @PathVariable EmergencyStatus status) {
        return ResponseEntity.ok(
                emergencyService.getEmergenciesByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmergencyResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam EmergencyStatus status) {
        return ResponseEntity.ok(emergencyService.updateStatus(id, status));
    }
}