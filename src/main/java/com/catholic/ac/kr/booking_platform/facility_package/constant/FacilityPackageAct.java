package com.catholic.ac.kr.booking_platform.facility_package.constant;

import lombok.Getter;

@Getter
public enum FacilityPackageAct {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String displayName;

    FacilityPackageAct(String displayName) {
        this.displayName = displayName;
    }
}
