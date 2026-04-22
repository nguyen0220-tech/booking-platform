package com.catholic.ac.kr.booking_platform.dto;

public record PendingEmailUpdate(
        Long userId,
        String newEmail
) {
}
