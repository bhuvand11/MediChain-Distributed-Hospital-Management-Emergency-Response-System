package com.medichain.bed_service.repository;

import com.medichain.bed_service.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByHospitalName(String hospitalName);
    List<Bed> findByStatus(String status);
    List<Bed> findByHospitalNameAndStatus(String hospitalName, String status);
    List<Bed> findByWardTypeAndStatus(String wardType, String status);
    List<Bed> findByHospitalNameAndWardTypeAndStatus(
            String hospitalName, String wardType, String status);
    Long countByHospitalNameAndStatus(String hospitalName, String status);
}