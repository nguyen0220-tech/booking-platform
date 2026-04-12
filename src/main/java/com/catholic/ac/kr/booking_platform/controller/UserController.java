package com.catholic.ac.kr.booking_platform.controller;

import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.enumdef.AdminActive;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("block")
    public ApiResponse<String> blockUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long userId,
            @RequestParam AdminActive active) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH: " + auth);

        return userService.blockUser(userDetails.getId(), userId, active);
    }

}
