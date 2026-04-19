package com.medichain.doctor_service.repository;


import com.medichain.doctor_service.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findByDepartment(String department);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByIsAvailableTrue();
    List<Doctor> findByHospitalName(String hospitalName);
}