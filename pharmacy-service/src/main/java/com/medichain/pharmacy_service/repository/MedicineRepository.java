package com.medichain.pharmacy_service.repository;

import com.medichain.pharmacy_service.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByCategory(String category);
    List<Medicine> findByIsAvailableTrue();
    List<Medicine> findByStockQuantityLessThan(Integer threshold);
    boolean existsByNameAndManufacturer(String name, String manufacturer);
}