package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FacilityInfoSportUpdateUpdateRequest extends FacilityInfoUpdateRequest {
    private BigDecimal hourPrice;
}
