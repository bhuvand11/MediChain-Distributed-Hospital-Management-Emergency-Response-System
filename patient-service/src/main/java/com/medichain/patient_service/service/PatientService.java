package com.medichain.patient_service.service;

import com.medichain.patient_service.dto.MedicalHistoryRequest;
import com.medichain.patient_service.dto.PatientRequest;
import com.medichain.patient_service.dto.PatientResponse;
import com.medichain.patient_service.entity.MedicalHistory;
import com.medichain.patient_service.entity.Patient;
import com.medichain.patient_service.exception.ResourceNotFoundException;
import com.medichain.patient_service.repository.MedicalHistoryRepository;
import com.medichain.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Patient already exists with email: " + request.getEmail());
        }
        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .allergies(request.getAllergies())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .build();
        return mapToResponse(patientRepository.save(patient));
    }

    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + id));
        return mapToResponse(patient);
    }

    public PatientResponse getPatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with email: " + email));
        return mapToResponse(patient);
    }

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + id));
        patient.setFullName(request.getFullName());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        return mapToResponse(patientRepository.save(patient));
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    public MedicalHistoryRequest addMedicalHistory(Long patientId,
                                                   MedicalHistoryRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId));
        MedicalHistory history = MedicalHistory.builder()
                .patient(patient)
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .medications(request.getMedications())
                .doctorName(request.getDoctorName())
                .hospitalName(request.getHospitalName())
                .diagnosisDate(request.getDiagnosisDate())
                .recoveryDate(request.getRecoveryDate())
                .notes(request.getNotes())
                .build();
        MedicalHistory saved = medicalHistoryRepository.save(history);
        return mapHistoryToDto(saved);
    }

    public List<MedicalHistoryRequest> getMedicalHistory(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return medicalHistoryRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapHistoryToDto)
                .collect(Collectors.toList());
    }

    private PatientResponse mapToResponse(Patient patient) {
        List<MedicalHistoryRequest> histories = medicalHistoryRepository
                .findByPatientId(patient.getId())
                .stream()
                .map(this::mapHistoryToDto)
                .collect(Collectors.toList());

        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .allergies(patient.getAllergies())
                .address(patient.getAddress())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .medicalHistories(histories)
                .createdAt(patient.getCreatedAt())
                .build();
    }

    private MedicalHistoryRequest mapHistoryToDto(MedicalHistory history) {
        MedicalHistoryRequest dto = new MedicalHistoryRequest();
        dto.setId(history.getId());
        dto.setDiagnosis(history.getDiagnosis());
        dto.setTreatment(history.getTreatment());
        dto.setMedications(history.getMedications());
        dto.setDoctorName(history.getDoctorName());
        dto.setHospitalName(history.getHospitalName());
        dto.setDiagnosisDate(history.getDiagnosisDate());
        dto.setRecoveryDate(history.getRecoveryDate());
        dto.setNotes(history.getNotes());
        return dto;
    }
}