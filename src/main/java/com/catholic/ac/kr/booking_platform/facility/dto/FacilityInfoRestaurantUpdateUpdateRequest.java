package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FoodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityInfoRestaurantUpdateUpdateRequest extends FacilityInfoUpdateRequest {
    private FoodType foodType;
}
