package com.catholic.ac.kr.booking_platform.auth.web;

import com.catholic.ac.kr.booking_platform.auth.core.AccountRecoveryService;
import com.catholic.ac.kr.booking_platform.auth.core.AuthService;
import com.catholic.ac.kr.booking_platform.auth.core.UserCheckInfoService;
import com.catholic.ac.kr.booking_platform.auth.dto.*;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
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
    private final AccountRecoveryService accountRecoveryService;
    private final UserCheckInfoService userCheckInfoService;

    @PostMapping("registry")
    public ApiResponse<String> registry(@RequestBody @Valid RegistryRequest request) {
        return authService.registry(request);
    }

    @PostMapping("verify")
    public ApiResponse<String> verifyNewUser(@RequestParam String token) {
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

    @PostMapping("confirm")
    public ApiResponse<String> checkExistInfo(@RequestBody CheckInfoRequest request) {
        return userCheckInfoService.checkExistInfo(request);
    }

    @PostMapping("find-username")
    public ApiResponse<String> forgotUsername(@RequestBody @Valid ForgotUsernameRequest request) {
        return accountRecoveryService.forgotUsername(request);
    }

    @PostMapping("find-password")
    public ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest username) {
        return accountRecoveryService.forgotPassword(username);
    }

    @PostMapping("reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid NewPasswordRequest request) {
        return accountRecoveryService.resetPassword(request);
    }
}
