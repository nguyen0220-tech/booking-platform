package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FacilityInfoSportUpdateRequest extends FacilityInfoRequest {
    private BigDecimal hourPrice;
}
