package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SportPackageRequest {
    private Long facilityId;
    private LocalTime startTime;
    private LocalTime endTime;
}
