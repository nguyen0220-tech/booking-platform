package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FacilityOptionRequest {
    private Long facilityId;
    private List<OptionStateRequest> optionStates;
}
