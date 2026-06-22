package com.catholic.ac.kr.booking_platform.review.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.review.core.ReviewService;
import com.catholic.ac.kr.booking_platform.review.dto.ReviewDTO;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewResolver {

    private final ReviewService reviewService;

    public ReviewResolver(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @SchemaMapping(typeName = "Facility", field = "reviews")
    public ListResponse<ReviewDTO> reviews(
            FacilityDTO facilityDTO,
            @Argument int page,
            @Argument int size) {

        return reviewService.getAllByFacilityId(facilityDTO.getId(), page, size);
    }

    @SchemaMapping(typeName = "Review", field = "reviewer")
    public UserDTO reviewer(ReviewDTO reviewDTO) {
        return new UserDTO(reviewDTO.getUserId());
    }
}
