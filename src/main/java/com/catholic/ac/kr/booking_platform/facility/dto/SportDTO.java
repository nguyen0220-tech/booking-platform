package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SportDTO {
    private Long id;
    private BigDecimal hourPrice;
}
