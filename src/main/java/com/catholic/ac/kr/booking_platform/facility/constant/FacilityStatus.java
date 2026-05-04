package com.catholic.ac.kr.booking_platform.facility.constant;

import lombok.Getter;

@Getter
public enum FacilityStatus {
    PENDING("접수중입니다"),
    APPROVED("승인되었습니다"),
    REJECTED("거절되었습니다");

    private final String displayStatus;
    FacilityStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }
}
