package com.catholic.ac.kr.booking_platform.enumdef;

import lombok.Getter;

@Getter
public enum FacilityStatus {
    PENDING("접수중"),
    APPROVED("승인됨"),
    REJECTED("저절됨");

    private final String displayStatus;
    FacilityStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }
}
