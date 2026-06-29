package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.facility.constant.PopularDestination;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilitySuggestionProjection;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityPublicService {
    private final FacilityRepository facilityRepository;

    public ListResponse<FacilityDTO> searchFacilitiesByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());

        Page<Facility> facilityPage = facilityRepository.findByKeyword(keyword, pageable);

        Page<FacilityDTO> facilityDTOPage = facilityPage.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> response = facilityDTOPage.getContent();

        return new ListResponse<>(
                response,
                new PageInfo(page, size, facilityPage.hasNext(), facilityPage.getTotalElements(), facilityPage.getTotalPages()));
    }

    public ListResponse<FacilityDTO> getFacilitiesInPopularDestination(
            PopularDestination destination, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());

        Page<Facility> facilityPage = facilityRepository
                .findAllByAddress(destination.getKorName(), pageable);

        Page<FacilityDTO> facilityDTOPage = facilityPage.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> response = facilityDTOPage.getContent();

        return new ListResponse<>(
                response,
                new PageInfo(page, size, facilityPage.hasNext(), facilityPage.getTotalElements(), facilityPage.getTotalPages()));
    }

    public ListResponse<FacilityDTO> suggestFacilities(Long userId) {
        LocalDate today = LocalDate.now();

        List<FacilitySuggestionProjection> projections = facilityRepository
                .findFacilitiesRecentlyAndTopSelling(userId, today, BookingStatus.COMPLETED.name());

        List<FacilityDTO> uniqueResults = projections.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        FacilitySuggestionProjection::getId, // Key để phân biệt trùng lặp
                        FacilityMapper::toFacilityDTO,
                        (existing, replacement) -> existing, // Nếu trùng thì giữ cái đầu tiên
                        LinkedHashMap::new // Giữ nguyên thứ tự ưu tiên (gần đây trước, bán chạy sau)
                ))
                .values()
                .stream()
                .toList();

        return new ListResponse<>(uniqueResults);

    }
}
