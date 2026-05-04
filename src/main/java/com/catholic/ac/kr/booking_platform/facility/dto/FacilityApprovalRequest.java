package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityApprovalRequest {
    private Long id;
    private FacilityStatus status;
    private String note;
}
