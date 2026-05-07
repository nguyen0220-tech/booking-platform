package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FacilityInfoMotelUpdateRequest extends FacilityInfoRequest {
    private BigDecimal hourPrice;
    private BigDecimal nightPrice;
}
