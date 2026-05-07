package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;

public interface FacilityUpdateHandler<F extends Facility, R extends FacilityInfoRequest> {
    FacilityType getFacilityType();

    ApiResponse<String> updateFacility(F facility, R request);
}
