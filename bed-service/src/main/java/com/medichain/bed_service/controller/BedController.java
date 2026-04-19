package com.medichain.bed_service.controller;

import com.medichain.bed_service.dto.BedRequest;
import com.medichain.bed_service.dto.BedResponse;
import com.medichain.bed_service.service.BedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    @PostMapping
    public ResponseEntity<BedResponse> createBed(
            @Valid @RequestBody BedRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bedService.createBed(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedResponse> getBedById(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.getBedById(id));
    }

    @GetMapping
    public ResponseEntity<List<BedResponse>> getAllBeds() {
        return ResponseEntity.ok(bedService.getAllBeds());
    }

    @GetMapping("/available/{hospitalName}")
    public ResponseEntity<List<BedResponse>> getAvailableBedsByHospital(
            @PathVariable String hospitalName) {
        return ResponseEntity.ok(bedService.getAvailableBedsByHospital(hospitalName));
    }

    @GetMapping("/count/{hospitalName}")
    public ResponseEntity<Long> getAvailableBedsCount(
            @PathVariable String hospitalName) {
        return ResponseEntity.ok(bedService.getAvailableBedsCount(hospitalName));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BedResponse> updateBedStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String patientName) {
        return ResponseEntity.ok(
                bedService.updateBedStatus(id, status, patientId, patientName));
    }

    @GetMapping("/ward/{wardType}/status/{status}")
    public ResponseEntity<List<BedResponse>> getBedsByWardAndStatus(
            @PathVariable String wardType,
            @PathVariable String status) {
        return ResponseEntity.ok(bedService.getBedsByWardAndStatus(wardType, status));
    }

    @GetMapping("/nearest/{wardType}")
    public ResponseEntity<String> findNearestHospitalWithBed(
            @PathVariable String wardType) {
        return ResponseEntity.ok(bedService.findNearestHospitalWithBed(wardType));
    }

    @GetMapping("/available/{hospitalName}/simple")
    public ResponseEntity<List<Map<String, Object>>> getAvailableBedsSimple(
            @PathVariable String hospitalName) {
        return ResponseEntity.ok(bedService.getAvailableBedsSimple(hospitalName));
    }
}