package com.medichain.pharmacy_service.service;


import com.medichain.pharmacy_service.dto.*;
import com.medichain.pharmacy_service.entity.Medicine;
import com.medichain.pharmacy_service.entity.Prescription;
import com.medichain.pharmacy_service.exception.ResourceNotFoundException;
import com.medichain.pharmacy_service.repository.MedicineRepository;
import com.medichain.pharmacy_service.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;

    public MedicineResponse addMedicine(MedicineRequest request) {
        Medicine medicine = Medicine.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .category(request.getCategory())
                .stockQuantity(request.getStockQuantity())
                .pricePerUnit(request.getPricePerUnit())
                .description(request.getDescription())
                .sideEffects(request.getSideEffects())
                .storageConditions(request.getStorageConditions())
                .build();
        return mapMedicineToResponse(medicineRepository.save(medicine));
    }

    public MedicineResponse getMedicineById(Long id) {
        return mapMedicineToResponse(medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with id: " + id)));
    }

    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(this::mapMedicineToResponse)
                .collect(Collectors.toList());
    }

    public List<MedicineResponse> getAvailableMedicines() {
        return medicineRepository.findByIsAvailableTrue().stream()
                .map(this::mapMedicineToResponse)
                .collect(Collectors.toList());
    }

    public List<MedicineResponse> getLowStockMedicines(Integer threshold) {
        return medicineRepository.findByStockQuantityLessThan(threshold).stream()
                .map(this::mapMedicineToResponse)
                .collect(Collectors.toList());
    }

    public MedicineResponse updateStock(Long id, Integer quantity) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicine not found with id: " + id));
        medicine.setStockQuantity(medicine.getStockQuantity() + quantity);
        return mapMedicineToResponse(medicineRepository.save(medicine));
    }

    public PrescriptionResponse issuePrescription(PrescriptionRequest request) {
        Prescription prescription = Prescription.builder()
                .patientId(request.getPatientId())
                .patientName(request.getPatientName())
                .doctorId(request.getDoctorId())
                .doctorName(request.getDoctorName())
                .medicines(request.getMedicines())
                .notes(request.getNotes())
                .build();
        return mapPrescriptionToResponse(prescriptionRepository.save(prescription));
    }

    public PrescriptionResponse getPrescriptionById(Long id) {
        return mapPrescriptionToResponse(prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with id: " + id)));
    }

    public List<PrescriptionResponse> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(this::mapPrescriptionToResponse)
                .collect(Collectors.toList());
    }

    public PrescriptionResponse dispensePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with id: " + id));
        if (prescription.getStatus().equals("DISPENSED")) {
            throw new RuntimeException("Prescription already dispensed");
        }
        prescription.setStatus("DISPENSED");
        prescription.setDispensedAt(LocalDateTime.now());
        return mapPrescriptionToResponse(prescriptionRepository.save(prescription));
    }

    private MedicineResponse mapMedicineToResponse(Medicine medicine) {
        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .manufacturer(medicine.getManufacturer())
                .category(medicine.getCategory())
                .stockQuantity(medicine.getStockQuantity())
                .pricePerUnit(medicine.getPricePerUnit())
                .description(medicine.getDescription())
                .sideEffects(medicine.getSideEffects())
                .storageConditions(medicine.getStorageConditions())
                .isAvailable(medicine.getIsAvailable())
                .createdAt(medicine.getCreatedAt())
                .build();
    }

    private PrescriptionResponse mapPrescriptionToResponse(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatientId())
                .patientName(prescription.getPatientName())
                .doctorId(prescription.getDoctorId())
                .doctorName(prescription.getDoctorName())
                .medicines(prescription.getMedicines())
                .status(prescription.getStatus())
                .notes(prescription.getNotes())
                .issuedAt(prescription.getIssuedAt())
                .dispensedAt(prescription.getDispensedAt())
                .build();
    }
}