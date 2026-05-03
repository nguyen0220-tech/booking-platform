package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class FacilityImageDTO {
    private Long entityId;
    private String imageUrl;
}
