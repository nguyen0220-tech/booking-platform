package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityPackageDTO {
    private Long id;
    private String facilityType;
    private FacilityPackageInfoDetails infoDetails;
}
