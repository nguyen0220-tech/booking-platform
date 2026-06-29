package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityQueryService;
import com.catholic.ac.kr.booking_platform.facility.core.admin.FacilityRegistrationCommandService;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityMapper;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationStatusDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.SecurityUtils;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.user.core.UserManageService;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FacilityRegistrationResolver {
    private final FacilityRegistrationCommandService facilityRegistrationCommandService;
    private final UserManageService userManageService;
    private final FacilityQueryService facilityQueryService;

    @QueryMapping
    public ListResponse<FacilityDTO> facilityRegistrationList(
            @Argument FacilityStatus status,
            @Argument int page,
            @Argument int size) {
        return facilityRegistrationCommandService.getFacilityRegistrations(status, page, size);
    }

    @QueryMapping
    public FacilityRegistrationDTO facilityRegistration(@Argument Long id) {
        return facilityRegistrationCommandService.getFacilityRegistration(id);
    }

    @BatchMapping(typeName = "FacilityRegistration", field = "facility")
    public Map<FacilityRegistrationDTO, FacilityDTO> facility(List<FacilityRegistrationDTO> registrations) {
        List<Long> facilityIds = getFacilityIds(registrations);

        Map<Long, FacilityDTO> facilityOMap = facilityQueryService.batchLoaderFacility(facilityIds);

        return registrations.stream()
                .collect(Collectors.toMap(
                        r -> r,
                        r -> facilityOMap.get(r.getFacilityId())
                ));
    }

    private List<Long> getFacilityIds(List<FacilityRegistrationDTO> registrations) {
        return registrations.stream()
                .map(FacilityRegistrationDTO::getFacilityId)
                .toList();
    }

    @BatchMapping(typeName = "FacilityRegistration", field = "reviewer")
    public Map<FacilityRegistrationDTO, UserDTO> reviewer(
            List<FacilityRegistrationDTO> registrations,
            Principal principal) {
        List<Long> reviewerIds = registrations.stream()
                .map(FacilityRegistrationDTO::getReviewerId)
                .toList();

        Map<Long, UserDTO> userMap = userManageService.batchLoaderUsers(reviewerIds, principal);

        return registrations.stream()
                .collect(Collectors.toMap(
                        r -> r,
                        r -> userMap.get(r.getReviewerId())
                ));
    }

    @BatchMapping(typeName = "Facility", field = "approvalStatus")
    public Map<FacilityDTO, FacilityRegistrationStatusDTO> approvalStatus(
            List<FacilityDTO> facilities,
            Principal principal) {

        UserDetailsImpl userDetails = SecurityUtils.getUserDetails(principal);

        if (userDetails == null) {
            Map<FacilityDTO, FacilityRegistrationStatusDTO> emptyResult = new HashMap<>();
            for (FacilityDTO facility : facilities) {
                emptyResult.put(facility, null);
            }
            return emptyResult;
        }

        Long currentUserId = userDetails.getId();
        boolean isAdmin = SecurityUtils.isAdmin(principal);

        // lọc ra các Facility ID mà User này ĐƯỢC QUYỀN xem (Owner,Admin)
        List<Long> authorizedFacilityIds = facilities.stream()
                .filter(f -> isAdmin || f.getOwnerId().equals(currentUserId))
                .map(FacilityDTO::getId)
                .toList();

        Map<Long, FacilityRegistrationStatusDTO> registrationMap = new HashMap<>();

        if (!authorizedFacilityIds.isEmpty()) {
            List<FacilityRegistration> facilityRegistrationList = facilityRegistrationCommandService
                    .getFacilityRegistrationByIds(authorizedFacilityIds);

            registrationMap = facilityRegistrationList.stream()
                    .collect(Collectors.toMap(
                            fr -> fr.getFacility().getId(),
                            FacilityMapper::convertToFacilityRegistrationDTO,
                            (existing, replacement) -> existing // đề phòng có 2 record trùng ID facility gây lỗi Duplicate Key
                    ));
        }

        Map<FacilityDTO, FacilityRegistrationStatusDTO> result = new HashMap<>();
        for (FacilityDTO facility : facilities) {
            if (isAdmin || facility.getOwnerId().equals(currentUserId)) {
                result.put(facility, registrationMap.get(facility.getId()));
            } else
                result.put(facility, null);
        }
        return result;
    }
}
