package com.catholic.ac.kr.booking_platform.controller;

import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.enumdef.AdminActive;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.service.UserManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserManageController {
    private final UserManageService userManageService;

    @PutMapping("act")
    public ApiResponse<String> actUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long userId,
            @RequestParam AdminActive active) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("AUTH: {}", auth);

        return userManageService.blockUser(userDetails.getId(), userId, active);
    }

}
