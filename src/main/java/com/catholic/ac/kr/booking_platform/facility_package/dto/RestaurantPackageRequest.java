package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RestaurantPackageRequest extends FacilityPackageRequest {
    @NotBlank(message = "입력 필수 항목입니다")
    private int capacity;

    @NotBlank(message = "입력 필수 항목입니다")
    private Set<RestaurantMenu> menus;
}
