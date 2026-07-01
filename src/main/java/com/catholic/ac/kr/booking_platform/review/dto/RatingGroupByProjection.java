package com.catholic.ac.kr.booking_platform.review.dto;

import com.catholic.ac.kr.booking_platform.review.constant.Rating;

public interface RatingGroupByProjection {
    Long getFacilityId();
    Rating getRating();
    Long getCount();
}
