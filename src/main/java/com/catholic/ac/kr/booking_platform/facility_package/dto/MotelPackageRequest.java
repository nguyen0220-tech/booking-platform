package com.catholic.ac.kr.booking_platform.facility_package.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class MotelPackageRequest extends FacilityPackageRequest{
    @NotBlank(message = "입력 필수 항목입니다")
    private LocalTime checkIn;

    @NotBlank(message = "입력 필수 항목입니다")
    private LocalTime checkOut;
}
