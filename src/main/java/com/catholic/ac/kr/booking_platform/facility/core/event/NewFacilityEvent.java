package com.catholic.ac.kr.booking_platform.facility.core.event;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;

public record NewFacilityEvent(
        Facility facility) {
}
