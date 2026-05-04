package com.catholic.ac.kr.booking_platform.facility.core.admin.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;

public class PendingState implements FacilityRegistrationState{

    @Override
    public void validateTransition(FacilityStatus status){
        if (status == FacilityStatus.PENDING){
            throw new BadRequestException(displayStatus().getDisplayStatus());        }
    }

    @Override
    public FacilityStatus displayStatus(){
        return FacilityStatus.PENDING;
    }
}
