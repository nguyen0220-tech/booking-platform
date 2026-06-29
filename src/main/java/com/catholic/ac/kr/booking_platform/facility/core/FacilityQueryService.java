package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.MotelFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.RestaurantFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.SportFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityQueryService {
    private final FacilityRepository facilityRepository;
    private final SportFacilityHandler sportFacilityHandler;
    private final MotelFacilityHandler motelFacilityHandler;
    private final RestaurantFacilityHandler restaurantFacilityHandler;

    @Cacheable(value = "facility-details", key = "#id")
    public FacilityDTO getFacilityById(Long id) {
        Facility facility = facilityRepository.findFacilityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        return FacilityMapper.toFacilityDTO(facility);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @Cacheable(value = "facilityPage", key = "{#page, #size}")
    public ListResponse<FacilityDTO> getFacilitiesByOwnerId(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Facility> facilityPage = facilityRepository.findByOwnerId(ownerId, pageable);

        Page<FacilityDTO> facilityDTOS = facilityPage.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> rs = facilityDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, facilityPage.hasNext()));
    }

    public Map<Long, FacilityDTO> batchLoaderFacility(List<Long> facilityIds) {
        List<Facility> facilities = facilityRepository.findAllById(facilityIds);

        return facilities.stream()
                .collect(Collectors.toMap(
                        Facility::getId,
                        FacilityMapper::toFacilityDTO
                ));
    }

    @Cacheable(value = "facilitySport", key = "{#ids}")
    public List<SportDTO> getFacilitySportByIds(List<Long> ids) {
        return ids != null ? sportFacilityHandler.getSpecificDTOs(ids) : List.of();
    }

    @Cacheable(value = "facilityMotel", key = "#ids")
    public List<MotelDTO> getFacilityMotelByIds(List<Long> ids) {
        return ids != null ? motelFacilityHandler.getSpecificDTOs(ids) : List.of();
    }

    @Cacheable(value = "facilityRestaurant", key = "#ids")
    public List<RestaurantDTO> getFacilityRestaurantByIds(List<Long> ids) {
        return ids != null ? restaurantFacilityHandler.getSpecificDTOs(ids) : List.of();
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public ListResponse<FacilityDTO> searchFacilityByKeyword(Long ownerId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Facility> facilityPage = facilityRepository.findByOwnerIdAndKeyword(ownerId, keyword, pageable);

        List<FacilityDTO> rs = facilityPage.stream()
                .map(FacilityMapper::toFacilityDTO)
                .toList();

        return new ListResponse<>(rs, new PageInfo(page, size, facilityPage.hasNext()));
    }
}
