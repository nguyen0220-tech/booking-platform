package com.catholic.ac.kr.booking_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class MotelDTO {
    private Long id;
    private BigDecimal nightPrice;
    private BigDecimal hourPrice;
}
