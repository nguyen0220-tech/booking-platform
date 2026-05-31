package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility_package.constant.FacilityPackageAct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityPackageUpdateRequest {
    private Long packageId;
    private FacilityPackageAct act;
}
