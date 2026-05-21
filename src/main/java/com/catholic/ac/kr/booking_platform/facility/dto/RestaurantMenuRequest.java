package com.catholic.ac.kr.booking_platform.facility.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantMenuRequest {
    @NotNull(message = "필수 함목입니다")
    private Long restaurantId;

    @NotBlank(message = "필수 함목입니다")
    private String name;

    private String description;

    @NotNull(message = "필수 함목입니다")
    @Positive(message = "가격은 0보다 커야 합니다")
    private BigDecimal price;

    private MultipartFile file;
}
