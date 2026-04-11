package com.catholic.ac.kr.booking_platform.controller;

import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("block")
    public ApiResponse<String> blockUser(@RequestParam Long userId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH: " + auth);

        return userService.blockUser(userId);
    }

}
