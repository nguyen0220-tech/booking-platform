package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacilityPackageDTO {
    private Long id;
    private String facilityType;

    private FacilityPackageInfoDetails infoDetails;
}
