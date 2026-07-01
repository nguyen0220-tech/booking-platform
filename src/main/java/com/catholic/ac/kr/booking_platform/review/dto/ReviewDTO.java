package com.catholic.ac.kr.booking_platform.review.dto;

import com.catholic.ac.kr.booking_platform.review.constant.Rating;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long userId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
}
