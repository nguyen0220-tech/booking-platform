package com.catholic.ac.kr.booking_platform.facility_package.web;

import com.catholic.ac.kr.booking_platform.facility_package.core.SportPackageService;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sport-package")
@RequiredArgsConstructor
public class SportPackageController {
    private final SportPackageService sportPackageService;

    @PostMapping
    public ApiResponse<String> createNewSportPackage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody SportPackageRequest request){
        return sportPackageService.createNewPackage(userDetails.getId(), request);
    }
}
