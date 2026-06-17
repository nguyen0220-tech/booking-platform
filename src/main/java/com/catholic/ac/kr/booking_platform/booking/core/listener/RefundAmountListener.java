package com.catholic.ac.kr.booking_platform.booking.core.listener;

import com.catholic.ac.kr.booking_platform.booking.core.RefundBookingAmountService;
import com.catholic.ac.kr.booking_platform.booking.core.event.BookingCancelledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RefundAmountListener {
    private final RefundBookingAmountService refundBookingAmountService;

    public RefundAmountListener(RefundBookingAmountService refundBookingAmountService) {
        this.refundBookingAmountService = refundBookingAmountService;
    }

    @EventListener
    public void onRefundAmountEvent(BookingCancelledEvent event) {
        refundBookingAmountService.refundAmountProcess(event);
    }
}
