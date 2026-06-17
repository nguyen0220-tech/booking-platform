package com.catholic.ac.kr.booking_platform.booking.core.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookingCancelledEvent {
    private Long userId;
    private BigDecimal amount;
    private LocalDate usageDate;
    private LocalDate cancelDate;
}
