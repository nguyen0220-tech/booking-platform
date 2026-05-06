package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_option;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityOption;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.dto.OptionStateRequest;

public interface FacilityOptionHandler {
    FacilityOption getFacilityOption();

    void setFacilityOption(Facility facility, OptionStateRequest request);
}
