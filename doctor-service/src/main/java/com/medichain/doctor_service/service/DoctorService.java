package com.medichain.doctor_service.service;


import com.medichain.doctor_service.dto.DoctorRequest;
import com.medichain.doctor_service.dto.DoctorResponse;
import com.medichain.doctor_service.dto.ShiftRequest;
import com.medichain.doctor_service.entity.Doctor;
import com.medichain.doctor_service.entity.Shift;
import com.medichain.doctor_service.exception.ResourceNotFoundException;
import com.medichain.doctor_service.repository.DoctorRepository;
import com.medichain.doctor_service.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ShiftRepository shiftRepository;

    public DoctorResponse createDoctor(DoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .specialization(request.getSpecialization())
                .department(request.getDepartment())
                .hospitalName(request.getHospitalName())
                .licenseNumber(request.getLicenseNumber())
                .experienceYears(request.getExperienceYears())
                .build();
        return mapToResponse(doctorRepository.save(doctor));
    }

    public DoctorResponse getDoctorById(Long id) {
        return mapToResponse(doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + id)));
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findByIsAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsByHospital(String hospitalName) {
        return doctorRepository.findByHospitalName(hospitalName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + id));
        doctor.setFullName(request.getFullName());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setDepartment(request.getDepartment());
        doctor.setHospitalName(request.getHospitalName());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setExperienceYears(request.getExperienceYears());
        return mapToResponse(doctorRepository.save(doctor));
    }

    public DoctorResponse toggleAvailability(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + id));
        doctor.setIsAvailable(!doctor.getIsAvailable());
        return mapToResponse(doctorRepository.save(doctor));
    }

    public Shift addShift(Long doctorId, ShiftRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));
        Shift shift = Shift.builder()
                .doctor(doctor)
                .shiftDate(request.getShiftDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .shiftType(request.getShiftType())
                .isOnCall(request.getIsOnCall() != null ? request.getIsOnCall() : false)
                .build();
        return shiftRepository.save(shift);
    }

    public List<Shift> getDoctorShifts(Long doctorId) {
        return shiftRepository.findByDoctorId(doctorId);
    }

    public List<Shift> getOnCallDoctors(LocalDate date) {
        return shiftRepository.findByShiftDateAndIsOnCallTrue(date);
    }

    private DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .department(doctor.getDepartment())
                .hospitalName(doctor.getHospitalName())
                .licenseNumber(doctor.getLicenseNumber())
                .experienceYears(doctor.getExperienceYears())
                .isAvailable(doctor.getIsAvailable())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
}