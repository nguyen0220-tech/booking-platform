package com.catholic.ac.kr.booking_platform.facility.core;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.api.FacilityProjection;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityApprovalService {

    private final FacilityApprovalRepository facilityApprovalRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<FacilityDTO> getFacilityRegistrations(FacilityStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FacilityProjection> projections = facilityApprovalRepository.findFacilityApprovalByStatus(status, pageable);

        Page<FacilityDTO> facilityDTOS = projections.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> rs = facilityDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, projections.hasNext()));
    }
}
