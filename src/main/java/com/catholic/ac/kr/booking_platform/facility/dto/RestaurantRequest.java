package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FoodType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
public class RestaurantRequest extends FacilityRequest {
    @NotNull(message = "입력 필수 항목입니다")
    private FoodType foodType;

    @NotNull(message = "입력 필수 항목입니다")
    private LocalTime openTime;

    @NotNull(message = "입력 필수 항목입니다")
    private LocalTime closeTime;
}
