package com.catholic.ac.kr.booking_platform.facility.core.admin.state;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;

import java.util.Map;

public class RegistrationStateProvider {
    private static final Map<FacilityStatus, FacilityRegistrationState> STATES = Map.of(
            FacilityStatus.PENDING, new PendingState(),
            FacilityStatus.APPROVED, new ApprovedState(),
            FacilityStatus.REJECTED, new RejectedState()
    );

    public static FacilityRegistrationState get(FacilityStatus status) {
        return STATES.getOrDefault(status, new PendingState());
    }
}
