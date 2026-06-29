package com.catholic.ac.kr.booking_platform.facility.dto;

public interface FacilitySuggestionProjection {
    Long getId();
    String getFacilityType();
    String getName();
    String getAddress();
    Integer getTotalReviews();
    Double getAverageRating();
}
