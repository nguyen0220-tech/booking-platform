package com.catholic.ac.kr.booking_platform.facility.core.admin.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRegistration;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRegistrationRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.User;

public interface FacilityRegistrationHandle {
    FacilityStatus getFacilityStatus();

    ApiResponse<String> handleFacilityRegistration(User admin, FacilityRegistration registration, FacilityRegistrationRequest request);
}
