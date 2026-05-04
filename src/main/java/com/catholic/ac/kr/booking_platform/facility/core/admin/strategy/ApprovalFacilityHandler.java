package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistrationRepository;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalFacilityHandler extends AbstractFacilityRegistrationHandler {
    private final FacilityRegistrationRepository facilityRegistrationRepository;

    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.APPROVED;
    }

    @Override
    public ApiResponse<String> handleFacilityRegistration(User admin, FacilityRegistration registration, FacilityRegistrationRequest request) {

        setHandleRegistration(admin, registration, request);

        facilityRegistrationRepository.save(registration);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 승인 처리되었습니다");
    }
}
