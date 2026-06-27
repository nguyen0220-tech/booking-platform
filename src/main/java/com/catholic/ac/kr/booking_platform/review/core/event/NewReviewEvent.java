package com.catholic.ac.kr.booking_platform.review.core.event;

import com.catholic.ac.kr.booking_platform.review.constant.Rating;

public record NewReviewEvent(
        Long facilityId,
        Rating rating
) {
}
