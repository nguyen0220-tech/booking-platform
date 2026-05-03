package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("facility")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @PostMapping
    public ApiResponse<String> createFacility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody FacilityRequest request
    ) {
        return facilityService.createFacility(userDetails.getId(), request);
    }

    @PostMapping("upload-images")
    public ApiResponse<List<String>> uploadImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute List<MultipartFile> images){

        return facilityService.uploadFacilityImage(userDetails.getId(), images);
    }
}
