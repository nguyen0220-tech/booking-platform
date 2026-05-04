package com.catholic.ac.kr.booking_platform.facility.core.admin.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;

public class RejectedState implements FacilityRegistrationState{
    @Override
    public void validateTransition(FacilityStatus status) {
        String exMessage = status.getDisplayStatus();
        if (status == FacilityStatus.REJECTED) {
            throw new BadRequestException("이미 " + displayStatus().getDisplayStatus());
        }
    }

    @Override
    public FacilityStatus displayStatus() {
        return FacilityStatus.REJECTED;
    }
}
