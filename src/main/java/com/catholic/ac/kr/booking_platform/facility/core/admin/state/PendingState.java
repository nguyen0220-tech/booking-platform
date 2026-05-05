package com.catholic.ac.kr.booking_platform.facility.core.admin.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class PendingState implements FacilityRegistrationState{
    @Override
    public void validateTransition(FacilityStatus targetStatus){
        if (targetStatus == FacilityStatus.PENDING){
            throw new BadRequestException(getStatus().getDisplayStatus());        }
    }

    @Override
    public FacilityStatus getStatus(){
        return FacilityStatus.PENDING;
    }
}
