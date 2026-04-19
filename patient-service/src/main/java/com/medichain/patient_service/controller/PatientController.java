package com.medichain.patient_service.controller;

import com.medichain.patient_service.dto.MedicalHistoryRequest;
import com.medichain.patient_service.dto.PatientRequest;
import com.medichain.patient_service.dto.PatientResponse;
import com.medichain.patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.createPatient(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PatientResponse> getPatientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(patientService.getPatientByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    @PostMapping("/{id}/medical-history")
    public ResponseEntity<MedicalHistoryRequest> addMedicalHistory(
            @PathVariable Long id,
            @Valid @RequestBody MedicalHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.addMedicalHistory(id, request));
    }

    @GetMapping("/{id}/medical-history")
    public ResponseEntity<List<MedicalHistoryRequest>> getMedicalHistory(
            @PathVariable Long id) {
        return ResponseEntity.ok(patientService.getMedicalHistory(id));
    }
}