package com.catholic.ac.kr.booking_platform.facility.core.event;

public record FacilityRejectionEvent(
        String ownerName,
        String ownerEmail,
        String facilityName
) {
}
