package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionStateRequest {
    private FacilityOption option;
    private boolean state;
}
