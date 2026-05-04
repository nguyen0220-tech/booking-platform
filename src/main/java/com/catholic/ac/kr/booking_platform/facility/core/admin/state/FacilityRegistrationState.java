package com.catholic.ac.kr.booking_platform.facility.core.admin.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;

public interface FacilityRegistrationState {
    void validateTransition(FacilityStatus targetStatus);

    FacilityStatus displayStatus();
}
