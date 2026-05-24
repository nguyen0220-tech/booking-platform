package com.catholic.ac.kr.booking_platform.facility_package.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class SportPackageRequest extends FacilityPackageRequest {
    @NotNull(message = "입력 필수 항목입니다")
    private LocalTime startTime;

    @NotNull(message = "입력 필수 항목입니다")
    private LocalTime endTime;
}
