package com.catholic.ac.kr.booking_platform.facility.core.provider.strategy_update;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.dto.FacilityInfoUpdateRequest;

import java.util.Objects;

public abstract class AbstractFacilityUpdateHandler<F extends Facility, R extends FacilityInfoUpdateRequest>
        implements FacilityUpdateHandler<F, R> {
    protected void updateFacilityInfo(F facility, R update) {
        facility.updateInfo(
                update.getName(),
                update.getDescription(),
                update.getInstruction(),
                update.getAddress()
        );
    }

    protected boolean isNotUpdated(F facility, R update) {
        // Sử dụng Objects.equals an toàn với Null và tận dụng Short-circuit
        return Objects.equals(update.getName(), facility.getName())
                && Objects.equals(update.getDescription(), facility.getDescription())
                && Objects.equals(update.getInstruction(), facility.getInstruction())
                && Objects.equals(update.getAddress(), facility.getAddress());
    }
}
