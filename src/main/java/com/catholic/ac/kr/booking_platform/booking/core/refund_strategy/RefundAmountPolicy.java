package com.catholic.ac.kr.booking_platform.booking.core.refund_strategy;

import com.catholic.ac.kr.booking_platform.booking.core.event.BookingCancelledEvent;

import java.time.LocalDate;

public interface RefundAmountPolicy {
    boolean isApplicable(LocalDate usageDate, LocalDate cancelDate);

    void processRefund(BookingCancelledEvent booking);
}
