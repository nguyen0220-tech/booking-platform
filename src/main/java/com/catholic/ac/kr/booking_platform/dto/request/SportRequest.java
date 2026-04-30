package com.catholic.ac.kr.booking_platform.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class SportRequest extends FacilityRequest{
    @NotNull(message = "입력 필수 항목입니다")
    @DecimalMin(value = "0.0", inclusive = false, message = "0보다 커야 합니다")
    private BigDecimal hourPrice;
}
