package com.catholic.ac.kr.booking_platform.review.dto;

import com.catholic.ac.kr.booking_platform.review.constant.Rating;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long bookingId;
    private Rating rating;
    private String content;
}
