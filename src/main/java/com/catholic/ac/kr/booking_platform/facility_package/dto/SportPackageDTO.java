package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SportPackageDTO {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
}
