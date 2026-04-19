package com.medichain.doctor_service.repository;

import com.medichain.doctor_service.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByDoctorId(Long doctorId);
    List<Shift> findByDoctorIdAndShiftDate(Long doctorId, LocalDate date);
    List<Shift> findByShiftDateAndIsOnCallTrue(LocalDate date);
}