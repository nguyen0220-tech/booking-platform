package com.catholic.ac.kr.booking_platform.user.web;

import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.constant.AdminActive;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.user.core.UserManageService;
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
