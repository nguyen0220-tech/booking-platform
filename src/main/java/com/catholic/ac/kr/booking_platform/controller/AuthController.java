package com.catholic.ac.kr.booking_platform.controller;

import com.catholic.ac.kr.booking_platform.dto.LoginRequest;
import com.catholic.ac.kr.booking_platform.dto.RegistryRequest;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.dto.response.LoginResponse;
import com.catholic.ac.kr.booking_platform.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("registry")
    public ApiResponse<String> registry(@RequestBody @Valid RegistryRequest request) {
        return authService.registry(request);
    }

    @PostMapping("verify")
    public ApiResponse<String> verifyNewUser(@RequestParam String token){
        return authService.verifyNewUser(token);
    }

    @PostMapping("login")
    public ApiResponse<LoginResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        return authService.login(request, httpRequest, httpResponse);
    }

    @DeleteMapping("logout")
    public ApiResponse<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return authService.logout(httpRequest, httpResponse);
    }
}
