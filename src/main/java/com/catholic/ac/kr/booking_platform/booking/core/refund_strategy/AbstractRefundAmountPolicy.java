package com.catholic.ac.kr.booking_platform.booking.core.refund_strategy;

import com.catholic.ac.kr.booking_platform.booking.core.event.BookingCancelledEvent;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
public abstract class AbstractRefundAmountPolicy implements RefundAmountPolicy {
    protected static final long LIMIT_5_DAYS = 5;
    protected static final long LIMIT_3_DAYS = 3;

    protected long calculateDaysUntilUsage(LocalDate cancelDate, LocalDate usageDate) {
        return ChronoUnit.DAYS.between(cancelDate, usageDate);
    }

    protected void refundAmount(Long userId, BigDecimal refundAmount, LocalDate cancelDate, LocalDate usageDate) {
        log.info("userId_{}에게 {}원을 환불하였습니다. (취소 {}, 이용 {}  ==> {} 일 전에 취소함",
                userId, refundAmount, cancelDate, usageDate, calculateDaysUntilUsage(cancelDate, usageDate));
    }

    @Override
    public void processRefund(BookingCancelledEvent booking) {
        Long userId = booking.getUserId();
        BigDecimal amount = booking.getAmount();
        BigDecimal refundAmount = getRefundAmount(amount);
        LocalDate cancelDate = booking.getCancelDate();
        LocalDate usageDate = booking.getUsageDate();

        refundAmount(userId, refundAmount, cancelDate, usageDate);
    }

    protected abstract BigDecimal getRefundAmount(BigDecimal amount);
}
