package com.catholic.ac.kr.booking_platform.event;

import com.catholic.ac.kr.booking_platform.model.Facility;

public record NewFacilityEvent(
        Facility facility) {
}
