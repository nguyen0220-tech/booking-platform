package com.catholic.ac.kr.booking_platform.facility.core.state;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.user.data.User;

public interface FacilityHandler {
    FacilityType getType();
    ApiResponse<String> create(User owner, FacilityRequest request);
}
