package com.catholic.ac.kr.booking_platform.booking.core;

import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.core.strategy.PaymentGatewayHandler;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final Map<PayMethod, PaymentGatewayHandler> gatewayHandlers;
    private final BookingRepository bookingRepository;

    public BookingService(List<PaymentGatewayHandler> gatewayList, BookingRepository bookingRepository) {
        this.gatewayHandlers = gatewayList.stream()
                .collect(Collectors.toMap(
                        PaymentGatewayHandler::getPayMethod, g -> g
                ));
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllByPackageIds( List<Long> packageIds) {
        return bookingRepository.findByFacilityPackageIds(packageIds);
    }

    public ApiResponse<String> createBooking(Long userId, BookingRequest request) {
        PayMethod method = request.getPayMethod();

        PaymentGatewayHandler gatewayHandler = gatewayHandlers.get(method);
        if (gatewayHandler == null) {
            throw new UnsupportedStrategyException("Unsupported payment gateway strategy: " + method);
        }

        return gatewayHandler.processPayment(userId, request);
    }
}
