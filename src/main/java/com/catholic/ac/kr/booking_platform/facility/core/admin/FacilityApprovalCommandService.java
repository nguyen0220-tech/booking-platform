package com.catholic.ac.kr.booking_platform.facility.core.admin;

import com.catholic.ac.kr.booking_platform.facility.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.admin.strategy.FacilityApprovalHandle;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApproval;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityApprovalRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityApprovalRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityProjection;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityApprovalCommandService {

    private final FacilityApprovalRepository facilityApprovalRepository;
    private final UserRepository userRepository;
    private final Map<FacilityStatus, FacilityApprovalHandle> facilityApprovalHandles;

    public FacilityApprovalCommandService(FacilityApprovalRepository facilityApprovalRepository, UserRepository userRepository,
                                          List<FacilityApprovalHandle> handles) {
        this.facilityApprovalRepository = facilityApprovalRepository;
        this.userRepository = userRepository;
        this.facilityApprovalHandles = handles.stream()
                .collect(Collectors.toMap(FacilityApprovalHandle::getFacilityStatus, h -> h));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<FacilityDTO> getFacilityRegistrations(FacilityStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FacilityProjection> projections = facilityApprovalRepository.findFacilityApprovalByStatus(status, pageable);

        Page<FacilityDTO> facilityDTOS = projections.map(FacilityMapper::toFacilityDTO);

        List<FacilityDTO> rs = facilityDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, projections.hasNext()));
    }

    public List<FacilityApproval> getFacilityApprovalByIds(List<Long> ids) {
        return ids != null ? facilityApprovalRepository.findAllById(ids) : List.of();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> handleFacilityRegistration(Long adminId, FacilityApprovalRequest request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("admin not found"));

        FacilityApproval approval = facilityApprovalRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility Approval Not Found"));

        String exMessage = "이미 " + approval.getStatus().getDisplayStatus();
        if (approval.getStatus().equals(FacilityStatus.APPROVED) ||
                approval.getStatus().equals(FacilityStatus.REJECTED)) {
            throw new BadRequestException(exMessage);
        }

        FacilityApprovalHandle handle = facilityApprovalHandles.get(request.getStatus());

        return handle.handleFacilityApproval(admin, approval, request);
    }
}