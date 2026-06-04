package com.catholic.ac.kr.booking_platform.booking.core.strategy;

import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;

public interface PaymentGatewayHandler {
    PayMethod getPayMethod();

    ApiResponse<String> processPayment(Long userId, BookingRequest request);
}

