package com.medichain.doctor_service.controller;

import com.medichain.doctor_service.dto.DoctorRequest;
import com.medichain.doctor_service.dto.DoctorResponse;
import com.medichain.doctor_service.dto.ShiftRequest;
import com.medichain.doctor_service.entity.Shift;
import com.medichain.doctor_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors() {
        return ResponseEntity.ok(doctorService.getAvailableDoctors());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(department));
    }

    @GetMapping("/hospital/{hospitalName}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByHospital(
            @PathVariable String hospitalName) {
        return ResponseEntity.ok(doctorService.getDoctorsByHospital(hospitalName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<DoctorResponse> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.toggleAvailability(id));
    }

    @PostMapping("/{id}/shifts")
    public ResponseEntity<Shift> addShift(
            @PathVariable Long id,
            @Valid @RequestBody ShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.addShift(id, request));
    }

    @GetMapping("/{id}/shifts")
    public ResponseEntity<List<Shift>> getDoctorShifts(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorShifts(id));
    }

    @GetMapping("/oncall")
    public ResponseEntity<List<Shift>> getOnCallDoctors(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(doctorService.getOnCallDoctors(date));
    }
}