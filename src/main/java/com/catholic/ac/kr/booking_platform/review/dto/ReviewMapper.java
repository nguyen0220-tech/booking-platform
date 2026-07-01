package com.catholic.ac.kr.booking_platform.review.dto;

import com.catholic.ac.kr.booking_platform.review.data.Review;

public class ReviewMapper {
    public static ReviewDTO toReviewDTO(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(review.getId());
        reviewDTO.setUserId(review.getReviewer().getId());
        reviewDTO.setRating(review.getRating().getValue());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setCreatedAt(review.getCreatedAt());

        return reviewDTO;
    }

    public static RatingGroupByDTO convertToRatingGroupByDTO(RatingGroupByProjection projection){
        return new RatingGroupByDTO(
                projection.getRating().getValue(),
                projection.getCount()
        );
    }
}
