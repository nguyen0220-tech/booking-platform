package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistrationRepository;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RejectFacilityHandler extends AbstractFacilityRegistrationHandler {
    private final FacilityRegistrationRepository facilityRegistrationRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.REJECTED;
    }

    @Override
    public ApiResponse<String> handleFacilityRegistration(User admin, FacilityRegistration registration, FacilityRegistrationRequest request) {
        Facility facility = registration.getFacility();
        facility.setSuspended(true);

        setHandleRegistration(admin, registration, request);

        facilityRegistrationRepository.save(registration);
        facilityRepository.save(facility);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 거절 처리되었습니다");
    }
}
