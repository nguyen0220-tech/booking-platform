package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.core.FacilityImageService;
import com.catholic.ac.kr.booking_platform.facility.core.provider.FacilityCommandService;
import com.catholic.ac.kr.booking_platform.facility.core.provider.FacilityUpdateService;
import com.catholic.ac.kr.booking_platform.facility.dto.AddImagesForFacilityRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoUpdateRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityOptionRequest;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
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
    private final FacilityCommandService facilityCommandService;
    private final FacilityImageService facilityImageService;
    private final FacilityUpdateService facilityUpdateService;

    @PostMapping
    public ApiResponse<String> createFacility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody FacilityRequest request
    ) {
        return facilityCommandService.createFacility(userDetails.getId(), request);
    }

    @PostMapping("upload-images")
    public ApiResponse<List<String>> uploadImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute List<MultipartFile> images) {

        return facilityImageService.uploadFacilityImage(userDetails.getId(), images);
    }

    @PostMapping("add-images")
    public ApiResponse<String> addImagesForFacility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AddImagesForFacilityRequest request
            ){
        return facilityImageService.addImagesForFacility(userDetails.getId(), request);
    }

    @PutMapping("option")
    public ApiResponse<String> updateFacilityOption(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody FacilityOptionRequest request) {
        return facilityUpdateService.updateFacilityOption(userDetails.getId(), request);
    }

    @PutMapping("info")
    public ApiResponse<String> updateFacilityInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody FacilityInfoUpdateRequest request) {
        return facilityUpdateService.updateFacilityInfo(userDetails.getId(), request);
    }
}
