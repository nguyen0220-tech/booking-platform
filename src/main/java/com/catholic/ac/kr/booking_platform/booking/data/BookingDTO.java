package com.catholic.ac.kr.booking_platform.booking.data;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long facilityPackageId;
    private Long facilityOwnerId;
    private Long facilityId;

    private BigDecimal amount;
    private BigDecimal basisPrice;
    private LocalDate usageDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private PayMethod payMethod;
    private LocalDateTime createdAt;
}
