package com.catholic.ac.kr.booking_platform.facility.constant;

import lombok.Getter;

@Getter
public enum FacilityType {
    SPORT("SportEquipment","스포츠"),
    MOTEL("Motel","모텔"),
    RESTAURANT("Restaurant","레스토랑");

    private final String displayName;
    private final String koreanName;
    FacilityType(String displayName, String koreanName) {
            this.displayName = displayName;
            this.koreanName = koreanName;

    }
}
