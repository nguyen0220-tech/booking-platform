package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoRequest;

public abstract class AbstractFacilityUpdateHandler<F extends Facility, R extends FacilityInfoRequest>
        implements FacilityUpdateHandler<F, R> {
    protected void updateFacilityInfo(F facility, R update) {
        facility.updateInfo(
                update.getName(),
                update.getDescription(),
                update.getInstruction(),
                update.getAddress()
        );
    }
}
