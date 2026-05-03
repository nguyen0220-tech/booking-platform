package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FacilityApprovalResolver {
    private final FacilityApprovalService facilityApprovalService;

    @QueryMapping
    public ListResponse<FacilityDTO> facilityApprovalList(
            @Argument FacilityStatus status,
            @Argument int page,
            @Argument int size) {
        return facilityApprovalService.getFacilityRegistrations(status, page, size);
    }
}
