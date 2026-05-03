package com.catholic.ac.kr.booking_platform.profile.data;

public record PendingEmailUpdate(
        Long userId,
        String newEmail
) {
}
