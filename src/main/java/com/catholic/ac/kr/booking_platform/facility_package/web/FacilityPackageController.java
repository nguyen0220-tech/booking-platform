package com.catholic.ac.kr.booking_platform.facility_package.web;

import com.catholic.ac.kr.booking_platform.facility_package.core.FacilityPackageCommandService;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageRequest;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageUpdateRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("facility-package")
@RequiredArgsConstructor
public class FacilityPackageController {

    private final FacilityPackageCommandService facilityPackageCommandService;

    @PostMapping
    public ApiResponse<String> createNSewPackage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid FacilityPackageRequest request) {
        return facilityPackageCommandService.createNewPackage(userDetails.getId(), request);
    }

    @PutMapping
    public ApiResponse<String> updateStatusPackage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid FacilityPackageUpdateRequest request) {
        return facilityPackageCommandService.updateStatusPackage(userDetails.getId(), request);
    }
}
