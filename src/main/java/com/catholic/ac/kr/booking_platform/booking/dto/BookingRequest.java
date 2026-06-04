package com.catholic.ac.kr.booking_platform.booking.dto;

import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingRequest {
    @NotNull
    private Long packageId;

    @NotNull(message = "입력 필수 항목입니다")
    private LocalDate usageDate;

    private LocalTime startTime;

    @NotNull(message = "입력 필수 항목입니다")
    private PayMethod payMethod;

    public BookingRequest(Long packageId, LocalDate usageDate, LocalTime of, PayMethod payMethod) {
        this.packageId = packageId;
        this.usageDate = usageDate;
        this.startTime = of;
        this.payMethod = payMethod;
    }
}
