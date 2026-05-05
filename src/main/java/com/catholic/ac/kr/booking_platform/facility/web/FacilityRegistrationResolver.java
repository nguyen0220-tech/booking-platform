package com.catholic.ac.kr.booking_platform.facility.web;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.core.admin.FacilityRegistrationCommandService;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FacilityRegistrationResolver {
    private final FacilityRegistrationCommandService facilityRegistrationCommandService;

    @QueryMapping
    public ListResponse<FacilityDTO> facilityRegistrationList(
            @Argument FacilityStatus status,
            @Argument int page,
            @Argument int size) {
        return facilityRegistrationCommandService.getFacilityRegistrations(status, page, size);
    }

    @QueryMapping
    public FacilityRegistrationDTO facilityRegistration(@Argument Long id) {
        return facilityRegistrationCommandService.getFacilityRegistration(id);
    }

    @SchemaMapping(typeName = "FacilityRegistration",field = "facility")
    public FacilityDTO facility(FacilityRegistrationDTO registration) {
        return new FacilityDTO(registration.getFacilityId(), registration.getFacilityType(), registration.getOwnerId(),registration.getId());
    }

    @SchemaMapping(typeName = "FacilityRegistration",field = "reviewer")
    public UserDTO reviewer(FacilityRegistrationDTO registration) {
        if (registration.getReviewerId() == null) {
            return null;
        }
        return new UserDTO(registration.getReviewerId());
    }
}
