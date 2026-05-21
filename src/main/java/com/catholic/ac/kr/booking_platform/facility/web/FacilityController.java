package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.core.FacilityImageService;
import com.catholic.ac.kr.booking_platform.facility.core.provider.FacilityCommandService;
import com.catholic.ac.kr.booking_platform.facility.core.provider.FacilityUpdateService;
import com.catholic.ac.kr.booking_platform.facility.core.provider.RestaurantMenuCommandService;
import com.catholic.ac.kr.booking_platform.facility.dto.*;
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
    private final RestaurantMenuCommandService restaurantMenuCommandService;

    @PostMapping
    public ApiResponse<String> createFacility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody FacilityRequest request
    ) {
        return facilityCommandService.createFacility(userDetails.getId(), request);
    }

    @PutMapping("cancel")
    public ApiResponse<String> cancelFacility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long facilityId) {
        return facilityCommandService.cancelFacility(userDetails.getId(), facilityId);
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
    ) {
        return facilityImageService.addImagesForFacility(userDetails.getId(), request);
    }

    @PostMapping("restaurant/add-menu")
    public ApiResponse<String> addNewMenuForRestaurant(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute @Valid RestaurantMenuRequest request) {
        return restaurantMenuCommandService.addNewMenuForRestaurant(userDetails.getId(), request);
    }

    @PutMapping("restaurant/update-menu")
    public ApiResponse<String> updateRestaurantMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid RestaurantMenuUpdateRequest request) {
        return restaurantMenuCommandService.updateMenuForRestaurant(userDetails.getId(), request);
    }

    @PutMapping("restaurant/handle-menu")
    public ApiResponse<String> handleRestaurantMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody RestaurantMenuCommandRequest request) {
        return restaurantMenuCommandService.restaurantMenuCommand(userDetails.getId(), request);
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
