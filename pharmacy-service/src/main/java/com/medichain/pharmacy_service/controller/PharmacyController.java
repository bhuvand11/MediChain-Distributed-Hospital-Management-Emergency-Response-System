package com.medichain.pharmacy_service.controller;

import com.medichain.pharmacy_service.dto.*;
import com.medichain.pharmacy_service.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping("/medicines")
    public ResponseEntity<MedicineResponse> addMedicine(
            @Valid @RequestBody MedicineRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pharmacyService.addMedicine(request));
    }

    @GetMapping("/medicines/{id}")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getMedicineById(id));
    }

    @GetMapping("/medicines")
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        return ResponseEntity.ok(pharmacyService.getAllMedicines());
    }

    @GetMapping("/medicines/available")
    public ResponseEntity<List<MedicineResponse>> getAvailableMedicines() {
        return ResponseEntity.ok(pharmacyService.getAvailableMedicines());
    }

    @GetMapping("/medicines/low-stock")
    public ResponseEntity<List<MedicineResponse>> getLowStockMedicines(
            @RequestParam(defaultValue = "10") Integer threshold) {
        return ResponseEntity.ok(pharmacyService.getLowStockMedicines(threshold));
    }

    @PatchMapping("/medicines/{id}/stock")
    public ResponseEntity<MedicineResponse> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(pharmacyService.updateStock(id, quantity));
    }

    @PostMapping("/prescriptions")
    public ResponseEntity<PrescriptionResponse> issuePrescription(
            @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pharmacyService.issuePrescription(request));
    }

    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(
            @PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getPrescriptionById(id));
    }

    @GetMapping("/prescriptions/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(pharmacyService.getPrescriptionsByPatient(patientId));
    }

    @PatchMapping("/prescriptions/{id}/dispense")
    public ResponseEntity<PrescriptionResponse> dispensePrescription(
            @PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.dispensePrescription(id));
    }
}