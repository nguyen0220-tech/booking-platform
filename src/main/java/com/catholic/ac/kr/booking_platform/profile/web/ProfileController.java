package com.catholic.ac.kr.booking_platform.profile.web;

import com.catholic.ac.kr.booking_platform.profile.data.ProfileDTO;
import com.catholic.ac.kr.booking_platform.profile.data.UpdateProfileRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.profile.core.ProfileService;
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
            @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(userDetails.getId(), request);
    }

    @PostMapping("verify")
    public ApiResponse<String> verifyUpdateEmail(@RequestParam String token) {
        return profileService.verifyUpdateEmailByToken(token);
    }


    @PostMapping
    public ApiResponse<String> uploadAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @ModelAttribute MultipartFile file) {

        return profileService.uploadAvatar(userDetails.getId(), file);
    }
}
