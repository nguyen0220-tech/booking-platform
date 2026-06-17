package com.catholic.ac.kr.booking_platform.booking.core.refund_strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PartialRefundPolicy extends AbstractRefundAmountPolicy {
    @Override
    public boolean isApplicable(LocalDate cancelDate, LocalDate usageDate) {
        long days = calculateDaysUntilUsage(cancelDate, usageDate);
        System.out.println("days: " + days);

        return AbstractRefundAmountPolicy.LIMIT_5_DAYS >= days &&
                days >= AbstractRefundAmountPolicy.LIMIT_3_DAYS;
    }

    @Override
    public BigDecimal getRefundAmount(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.5));
    }
}
