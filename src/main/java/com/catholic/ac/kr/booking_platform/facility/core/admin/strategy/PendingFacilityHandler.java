package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;

import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PendingFacilityHandler implements FacilityRegistrationHandle {
    @Override
    public FacilityStatus getFacilityStatus() {
        return FacilityStatus.PENDING;
    }

    @Override
    public ApiResponse<String> handleFacilityRegistration(User admin, FacilityRegistration registration, FacilityRegistrationRequest request) {
        throw new BadRequestException("비정상적인 처리입니다");
    }
}
