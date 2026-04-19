package com.medichain.emergency_service.service;

import com.medichain.emergency_service.feign.BedServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedServiceWrapper {

    private final BedServiceClient bedServiceClient;

    @CircuitBreaker(name = "bedService", fallbackMethod = "findHospitalFallback")
    @Retry(name = "bedService")
    public String findNearestHospitalWithBed(String wardType) {
        return bedServiceClient.findNearestHospitalWithBed(wardType);
    }

    @CircuitBreaker(name = "bedService", fallbackMethod = "getAvailableBedsFallback")
    @Retry(name = "bedService")
    public List<Object> getAvailableBedsByHospital(String hospitalName) {
        return bedServiceClient.getAvailableBedsByHospital(hospitalName);
    }

    @CircuitBreaker(name = "bedService", fallbackMethod = "updateBedStatusFallback")
    @Retry(name = "bedService")
    public Object updateBedStatus(Long id, String status,
                                  Long patientId, String patientName) {
        return bedServiceClient.updateBedStatus(id, status, patientId, patientName);
    }

    public String findHospitalFallback(String wardType, Exception e) {
        log.error("Bed service down — cannot find hospital: {}", e.getMessage());
        return null;
    }

    public List<Object> getAvailableBedsFallback(String hospitalName, Exception e) {
        log.error("Bed service down — cannot get beds: {}", e.getMessage());
        return Collections.emptyList();
    }

    public Object updateBedStatusFallback(Long id, String status,
                                          Long patientId, String patientName, Exception e) {
        log.error("Bed service down — cannot update bed status: {}", e.getMessage());
        return null;
    }
}