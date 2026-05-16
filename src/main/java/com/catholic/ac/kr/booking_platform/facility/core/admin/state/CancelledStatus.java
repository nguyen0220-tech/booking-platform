package com.catholic.ac.kr.booking_platform.facility.core.admin.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class CancelledStatus implements FacilityRegistrationState{
    @Override
    public void validateTransition(FacilityStatus targetStatus) {
        throw new BadRequestException("이미 " + getStatus().getDisplayStatus());
    }

    @Override
    public FacilityStatus getStatus() {
        return FacilityStatus.CANCELLED;
    }
}
