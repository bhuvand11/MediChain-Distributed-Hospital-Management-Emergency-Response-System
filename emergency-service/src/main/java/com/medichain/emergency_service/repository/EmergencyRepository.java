package com.medichain.emergency_service.repository;

import com.medichain.emergency_service.entity.Emergency;
import com.medichain.emergency_service.enums.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    List<Emergency> findByPatientId(Long patientId);
    List<Emergency> findByStatus(EmergencyStatus status);
    List<Emergency> findByAssignedHospitalName(String hospitalName);
}