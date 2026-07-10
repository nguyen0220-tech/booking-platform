package com.catholic.ac.kr.booking_platform.review.dto;

import com.catholic.ac.kr.booking_platform.review.constant.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewEligibility {
    private ReviewStatus reviewStatus;
}
