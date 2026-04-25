package com.catholic.ac.kr.booking_platform.event;

public record ForgotPasswordEvent(
        String userFullName,
        String email,
        String token
) {
}
