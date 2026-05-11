package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilitySummaryProjection;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.MotelFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.RestaurantFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.core.provider.strategy.SportFacilityHandler;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.MotelDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.SportDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityQueryService {
    private final FacilityRepository facilityRepository;
    private final SportFacilityHandler sportFacilityHandler;
    private final MotelFacilityHandler motelFacilityHandler;
    private final RestaurantFacilityHandler restaurantFacilityHandler;

    @Cacheable(value = "facility-details", key = "#id")
    public FacilityDTO getFacilityById(Long ownerId, Long id) {
        FacilitySummaryProjection projection = facilityRepository.findFacilityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        if (!ownerId.equals(projection.getOwnerId())) {
            throw new AccessDeniedException("access denied");
        }
        return FacilityMapper.toFacilityDTO(projection);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @Cacheable(value = "facilityPage", key = "{#page, #size}")
    public ListResponse<FacilityDTO> getFacilitiesByOwnerId(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FacilitySummaryProjection> facilityProjections = facilityRepository.findByOwnerId(ownerId, pageable);

        Page<FacilityDTO> facilityDTOS = facilityProjections.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> rs = facilityDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, facilityProjections.hasNext()));
    }

    @Cacheable(value = "facilityInfos",key = "#facilityIds")
    public List<Facility> getFacilityByIds(List<Long> facilityIds) {
        return facilityRepository.findAllById(facilityIds);
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

        Page<FacilitySummaryProjection> projection = facilityRepository.findByOwnerIdAndKeyword(ownerId, keyword, pageable);

        List<FacilityDTO> rs = projection.stream()
                .map(FacilityMapper::toFacilityDTO)
                .toList();

        return new ListResponse<>(rs, new PageInfo(page, size, projection.hasNext()));
    }
}
