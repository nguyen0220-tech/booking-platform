package com.catholic.ac.kr.booking_platform.controller;

import com.catholic.ac.kr.booking_platform.dto.ProfileDTO;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ApiResponse<ProfileDTO> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return profileService.getProfile(userDetails.getId());
    }

    @PostMapping
    public ApiResponse<String> uploadAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute MultipartFile file) {

        return profileService.uploadAvatar(userDetails.getId(), file);
    }
}
