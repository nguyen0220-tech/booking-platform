package com.catholic.ac.kr.booking_platform.facility_package.core.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility_package.dto.FacilityPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;

public interface FacilityPackageHandler <T extends FacilityPackageRequest> {
    FacilityType getFacilityType();

    ApiResponse<String> createPackage(Long ownerId, Long facilityId, T request);
}
