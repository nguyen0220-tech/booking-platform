package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FoodType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class FacilityInfoRestaurantUpdateRequest extends FacilityInfoUpdateRequest {
    private FoodType foodType;
    private LocalTime openTime;
    private LocalTime closeTime;
}
