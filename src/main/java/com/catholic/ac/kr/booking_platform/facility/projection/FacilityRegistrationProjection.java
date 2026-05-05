package com.catholic.ac.kr.booking_platform.facility.projection;

import java.time.LocalDateTime;

public interface FacilityRegistrationProjection {
    Long getId();
    Long getFacilityId();
    String getFacilityType();
    String getStatus();
    String getNote();
    Long  getOwnerId();
    Long getReviewerId();
    LocalDateTime getLastUpdateAt();
}
