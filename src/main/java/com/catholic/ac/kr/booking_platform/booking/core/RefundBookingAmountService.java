package com.catholic.ac.kr.booking_platform.booking.core;

import com.catholic.ac.kr.booking_platform.booking.core.event.BookingCancelledEvent;
import com.catholic.ac.kr.booking_platform.booking.core.refund_strategy.RefundAmountPolicy;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RefundBookingAmountService {
    private final List<RefundAmountPolicy> policies;

    public RefundBookingAmountService(List<RefundAmountPolicy> policies) {
        this.policies = policies;
    }

    public void refundAmountProcess(BookingCancelledEvent booking) {
        LocalDate usageDate = booking.getUsageDate();
        LocalDate cancelDate = LocalDate.now();

        RefundAmountPolicy policy = policies.stream()
                .filter(p-> p.isApplicable(cancelDate, usageDate))
                .findFirst()
                .orElseThrow(()-> new UnsupportedStrategyException("환불 정책"));

        policy.processRefund(booking);
    }
}
