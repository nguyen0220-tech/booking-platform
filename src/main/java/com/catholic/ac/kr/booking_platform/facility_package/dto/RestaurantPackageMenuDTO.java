package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantPackageMenuDTO {
    private Long packageId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
