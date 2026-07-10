package com.catholic.ac.kr.booking_platform.review.constant;

public enum ReviewStatus {
    ELIGIBLE,           // 평가 가능
    ALREADY_REVIEWED,   // 평가 완료
    EXPIRED,            // 평가 기간 만료
    NOT_YET_COMPLETED,   // 예약 미사용
    BOOKING_CANCELLED,
}
