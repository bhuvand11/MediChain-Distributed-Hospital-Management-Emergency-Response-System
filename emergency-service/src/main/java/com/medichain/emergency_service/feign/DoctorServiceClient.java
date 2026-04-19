package com.medichain.emergency_service.feign;

import com.medichain.emergency_service.dto.DoctorAssignmentEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "doctor-service")
public interface DoctorServiceClient {

    @GetMapping("/api/doctors/available")
    List<Object> getAvailableDoctors();

    @PatchMapping("/api/doctors/{id}/availability")
    Object toggleDoctorAvailability(@PathVariable Long id);
}