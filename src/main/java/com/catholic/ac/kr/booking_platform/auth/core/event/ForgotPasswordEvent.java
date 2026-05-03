package com.catholic.ac.kr.booking_platform.auth.core.event;

public record ForgotPasswordEvent(
        String userFullName,
        String email,
        String token
) {
}
