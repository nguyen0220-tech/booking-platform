package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
public class SportPackageDTO {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal totalPrice;

    private FacilityDTO facility;
}
