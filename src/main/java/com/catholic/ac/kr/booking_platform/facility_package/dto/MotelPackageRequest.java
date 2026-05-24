package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility_package.constant.PricingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class MotelPackageRequest extends FacilityPackageRequest{
    @NotNull(message = "입력 필수 항목입니다")
    private LocalTime checkIn;

    @NotNull(message = "입력 필수 항목입니다")
    private LocalTime checkOut;

    @NotNull(message = "입력 필수 항목입니다")
    private PricingType pricingType;
}
