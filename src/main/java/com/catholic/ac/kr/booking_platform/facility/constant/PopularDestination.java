package com.catholic.ac.kr.booking_platform.facility.constant;

import lombok.Getter;

@Getter
public enum PopularDestination {
    SEOUL("서울"),
    BUSAN("부산"),
    JEJU("제주"),
    GANGNEUNG("강릉");

    private final String korName;

    PopularDestination(String korName) {
        this.korName = korName;
    }
}
