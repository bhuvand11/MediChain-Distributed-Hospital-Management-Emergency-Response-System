package com.medichain.emergency_service.service;

import com.medichain.emergency_service.feign.DoctorServiceClient;
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
public class DoctorServiceWrapper {

    private final DoctorServiceClient doctorServiceClient;

    @CircuitBreaker(name = "doctorService", fallbackMethod = "getAvailableDoctorsFallback")
    @Retry(name = "doctorService")
    public List<Object> getAvailableDoctors() {
        return doctorServiceClient.getAvailableDoctors();
    }

    @CircuitBreaker(name = "doctorService", fallbackMethod = "toggleAvailabilityFallback")
    @Retry(name = "doctorService")
    public Object toggleDoctorAvailability(Long id) {
        return doctorServiceClient.toggleDoctorAvailability(id);
    }

    public List<Object> getAvailableDoctorsFallback(Exception e) {
        log.error("Doctor service down — cannot get doctors: {}", e.getMessage());
        return Collections.emptyList();
    }

    public Object toggleAvailabilityFallback(Long id, Exception e) {
        log.error("Doctor service down — cannot toggle availability: {}",
                e.getMessage());
        return null;
    }
}