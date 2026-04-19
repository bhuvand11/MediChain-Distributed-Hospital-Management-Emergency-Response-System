package com.medichain.emergency_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "bed-service")
public interface BedServiceClient {

    @GetMapping("/api/beds/nearest/{wardType}")
    String findNearestHospitalWithBed(@PathVariable String wardType);

    @GetMapping("/api/beds/available/{hospitalName}/simple")
    List<Object> getAvailableBedsByHospital(@PathVariable String hospitalName);

    @PatchMapping("/api/beds/{id}/status")
    Object updateBedStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String patientName);
}