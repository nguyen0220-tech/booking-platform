package com.catholic.ac.kr.booking_platform.controller;

import com.catholic.ac.kr.booking_platform.dto.ProfileDTO;
import com.catholic.ac.kr.booking_platform.dto.request.UpdateProfileRequest;
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

    @PutMapping
    public ApiResponse<String> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateProfileRequest request){
        return profileService.updateProfile(userDetails.getId(),request);
    }

    @PostMapping("verify")
    public ApiResponse<String> verifyUpdateEmail(@RequestParam String token){
        return profileService.verifyUpdateEmailByToken(token);
    }


    @PostMapping
    public ApiResponse<String> uploadAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute MultipartFile file) {

        return profileService.uploadAvatar(userDetails.getId(), file);
    }
}
