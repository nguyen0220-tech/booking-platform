package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_option;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityOption;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.dto.OptionStateRequest;
import org.springframework.stereotype.Component;

@Component
public class FacilityActiveOption implements FacilityOptionHandler {
    @Override
    public FacilityOption getFacilityOption() {
        return FacilityOption.ACTIVE;
    }

    @Override
    public void setFacilityOption( Facility facility, OptionStateRequest request) {
        boolean targetState = request.isState();

        if (facility.isActive() != targetState) {
            facility.setActive(targetState);
        }
    }
}
