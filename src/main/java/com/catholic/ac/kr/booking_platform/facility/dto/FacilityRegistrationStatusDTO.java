package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class FacilityRegistrationStatusDTO {
    private String status;
    private String note;
    private LocalDateTime lastUpdateAt;
}
