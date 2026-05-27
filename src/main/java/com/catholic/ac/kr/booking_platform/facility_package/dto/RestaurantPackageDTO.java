package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPackageDTO {
    private Long id;
    private int maxCapacity;
}
