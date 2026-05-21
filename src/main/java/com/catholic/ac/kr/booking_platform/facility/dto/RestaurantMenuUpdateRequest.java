package com.catholic.ac.kr.booking_platform.facility.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantMenuUpdateRequest {
    private Long menuId;
    private String name;
    private String description;
    @Positive(message = "가격은 0보다 커야 합니다")
    private BigDecimal price;
}
