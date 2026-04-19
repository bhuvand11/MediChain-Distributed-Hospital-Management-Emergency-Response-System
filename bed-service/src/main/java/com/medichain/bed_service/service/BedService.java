package com.medichain.bed_service.service;

import com.medichain.bed_service.dto.BedRequest;
import com.medichain.bed_service.dto.BedResponse;
import com.medichain.bed_service.entity.Bed;
import com.medichain.bed_service.exception.ResourceNotFoundException;
import com.medichain.bed_service.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BedService {

    private final BedRepository bedRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BED_CACHE_PREFIX = "bed:available:";

    public BedResponse createBed(BedRequest request) {
        Bed bed = Bed.builder()
                .bedNumber(request.getBedNumber())
                .wardType(request.getWardType())
                .hospitalName(request.getHospitalName())
                .build();
        BedResponse response = mapToResponse(bedRepository.save(bed));
        updateAvailabilityCache(request.getHospitalName());
        return response;
    }

    public BedResponse getBedById(Long id) {
        return mapToResponse(bedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bed not found with id: " + id)));
    }

    public List<BedResponse> getAllBeds() {
        return bedRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BedResponse> getAvailableBedsByHospital(String hospitalName) {
        String cacheKey = BED_CACHE_PREFIX + hospitalName;
        Object cached = redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            return (List<BedResponse>) cached;
        }

        List<BedResponse> beds = bedRepository
                .findByHospitalNameAndStatus(hospitalName, "AVAILABLE")
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(cacheKey, beds, 5, TimeUnit.MINUTES);
        return beds;
    }

    public Long getAvailableBedsCount(String hospitalName) {
        return bedRepository.countByHospitalNameAndStatus(hospitalName, "AVAILABLE");
    }

    public BedResponse updateBedStatus(Long id, String status,
                                       Long patientId, String patientName) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bed not found with id: " + id));
        bed.setStatus(status);
        bed.setPatientId(patientId);
        bed.setPatientName(patientName);
        BedResponse response = mapToResponse(bedRepository.save(bed));
        updateAvailabilityCache(bed.getHospitalName());
        return response;
    }

    public List<BedResponse> getBedsByWardAndStatus(String wardType, String status) {
        return bedRepository.findByWardTypeAndStatus(wardType, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public String findNearestHospitalWithBed(String wardType) {
        List<Bed> availableBeds = bedRepository
                .findByWardTypeAndStatus(wardType, "AVAILABLE");
        if (availableBeds.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No available beds found for ward type: " + wardType);
        }
        return availableBeds.get(0).getHospitalName();
    }

    private void updateAvailabilityCache(String hospitalName) {
        String cacheKey = BED_CACHE_PREFIX + hospitalName;
        redisTemplate.delete(cacheKey);
    }

    private BedResponse mapToResponse(Bed bed) {
        return BedResponse.builder()
                .id(bed.getId())
                .bedNumber(bed.getBedNumber())
                .wardType(bed.getWardType())
                .hospitalName(bed.getHospitalName())
                .status(bed.getStatus())
                .patientId(bed.getPatientId())
                .patientName(bed.getPatientName())
                .createdAt(bed.getCreatedAt())
                .updatedAt(bed.getUpdatedAt())
                .build();
    }
}