package com.catholic.ac.kr.booking_platform.facility_package.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RestaurantPackageRequest extends FacilityPackageRequest {
    @NotNull(message = "입력 필수 항목입니다")
    @Min(value = 1, message = "1명 이상이어야 합니다")
    private Integer capacity;

    @NotEmpty(message = "입력 필수 항목입니다")
    private Set<Long> menuIds;
}
