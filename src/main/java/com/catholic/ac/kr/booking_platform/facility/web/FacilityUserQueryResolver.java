package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.constant.PopularDestination;
import com.catholic.ac.kr.booking_platform.facility.core.FacilityPublicService;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FacilityUserQueryResolver {

    private final FacilityPublicService facilityPublicService;

    @QueryMapping
    public ListResponse<FacilityDTO> facilitiesWithKeyword(
            @Argument String keyword,
            @Argument int page,
            @Argument int size
    ) {
        return facilityPublicService.searchFacilitiesByKeyword(keyword, page, size);
    }

    @QueryMapping
    public ListResponse<FacilityDTO> facilitiesInPopularDestination(
            @Argument PopularDestination destination,
            @Argument int page,
            @Argument int size
    ) {
        return facilityPublicService.getFacilitiesInPopularDestination(destination, page, size);
    }

    @QueryMapping
    public ListResponse<FacilityDTO> facilitiesSuggestion(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return facilityPublicService.suggestFacilities(userDetails.getId());
    }
}
