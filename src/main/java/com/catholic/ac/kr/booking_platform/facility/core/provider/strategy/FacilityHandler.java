package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.user.data.User;

import java.util.List;

public interface FacilityHandler<T> {
    FacilityType getType();
    List<T> getSpecificDTOs(List<Long> ids);
    ApiResponse<String> create(User owner, FacilityRequest request);
}
