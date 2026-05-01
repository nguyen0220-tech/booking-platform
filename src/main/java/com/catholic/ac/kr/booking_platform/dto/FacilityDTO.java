package com.catholic.ac.kr.booking_platform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityDTO {
    private Long id;
    private String facilityType;
    private Long ownerId;
}
