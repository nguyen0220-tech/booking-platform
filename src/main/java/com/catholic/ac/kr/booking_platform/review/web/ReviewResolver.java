package com.catholic.ac.kr.booking_platform.review.web;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.review.core.ReviewService;
import com.catholic.ac.kr.booking_platform.review.dto.RatingGroupByDTO;
import com.catholic.ac.kr.booking_platform.review.dto.RatingGroupByProjection;
import com.catholic.ac.kr.booking_platform.review.dto.ReviewDTO;
import com.catholic.ac.kr.booking_platform.review.dto.ReviewMapper;
import com.catholic.ac.kr.booking_platform.user.core.UserManageService;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ReviewResolver {

    private final ReviewService reviewService;
    private final UserManageService userManageService;

    public ReviewResolver(ReviewService reviewService, UserManageService userManageService) {
        this.reviewService = reviewService;
        this.userManageService = userManageService;
    }

    @SchemaMapping(typeName = "Facility", field = "reviews")
    public ListResponse<ReviewDTO> reviews(
            FacilityDTO facilityDTO,
            @Argument int page,
            @Argument int size) {

        return reviewService.getAllByFacilityId(facilityDTO.getId(), page, size);
    }

    @BatchMapping(typeName = "Review", field = "reviewer")
    public Map<ReviewDTO, UserDTO> reviewer(
            List<ReviewDTO> reviews,
            Principal principal) {

        List<Long> userIds = reviews.stream()
                .map(ReviewDTO::getUserId)
                .toList();

        Map<Long, UserDTO> userMap = userManageService.batchLoaderUsers(userIds, principal);

        return reviewUserMapping(userMap, reviews);
    }

    private Map<ReviewDTO, UserDTO> reviewUserMapping(Map<Long, UserDTO> userMap, List<ReviewDTO> reviews) {
        return reviews.stream()
                .collect(Collectors.toMap(
                        r -> r,
                        r -> userMap.get(r.getUserId())
                ));
    }

    @BatchMapping(typeName = "Facility")
    public Map<FacilityDTO, List<RatingGroupByDTO>> ratingGroupBy(List<FacilityDTO> facilities) {
        List<Long> facilityIds = facilities.stream()
                .map(FacilityDTO::getId)
                .toList();

        List<RatingGroupByProjection> groupList = reviewService.getReviewGroupByDTO(facilityIds);

        Map<Long, List<RatingGroupByDTO>> mapByFacilityId = groupList.stream()
                .collect(Collectors.groupingBy(
                        RatingGroupByProjection::getFacilityId,
                        Collectors.mapping(
                                ReviewMapper::convertToRatingGroupByDTO,
                                Collectors.toList()
                        )
                ));

        return facilities.stream()
                .collect(Collectors.toMap(
                        f -> f,
                        f -> mapByFacilityId.getOrDefault(f.getId(), Collections.emptyList())));
    }
}
