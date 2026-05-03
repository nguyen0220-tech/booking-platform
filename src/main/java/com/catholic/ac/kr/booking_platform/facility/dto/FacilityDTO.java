package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityDTO {
    private Long id;
    private String facilityType;
    private Long ownerId;
}
