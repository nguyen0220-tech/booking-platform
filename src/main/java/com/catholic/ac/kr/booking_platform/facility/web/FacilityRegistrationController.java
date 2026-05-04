package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.core.admin.FacilityApprovalCommandService;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("facility-approval")
@RequiredArgsConstructor
public class FacilityRegistrationController {

    private final FacilityApprovalCommandService facilityApprovalCommandService;

    @PostMapping
    public ApiResponse<String> facilityRegistrationHandler(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody FacilityRegistrationRequest request) {
        return facilityApprovalCommandService.handleFacilityRegistration(userDetails.getId(), request);
    }
}
