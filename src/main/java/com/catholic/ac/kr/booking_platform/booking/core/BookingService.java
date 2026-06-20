package com.catholic.ac.kr.booking_platform.booking.core;

import com.catholic.ac.kr.booking_platform.booking.dto.BookingMapper;
import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.core.strategy.PaymentGatewayHandler;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailabilityRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.UnsupportedStrategyException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final Map<PayMethod, PaymentGatewayHandler> gatewayHandlers;
    private final BookingRepository bookingRepository;
    private final PackageAvailabilityRepository packageAvailabilityRepository;
    private final ApplicationEventPublisher publisher;

    public BookingService(List<PaymentGatewayHandler> gatewayList, BookingRepository bookingRepository,
                          PackageAvailabilityRepository packageAvailabilityRepository, ApplicationEventPublisher publisher) {
        this.gatewayHandlers = gatewayList.stream()
                .collect(Collectors.toMap(
                        PaymentGatewayHandler::getPayMethod, g -> g
                ));
        this.bookingRepository = bookingRepository;
        this.packageAvailabilityRepository = packageAvailabilityRepository;
        this.publisher = publisher;
    }

    public ApiResponse<String> createBooking(Long userId, BookingRequest request) {
        PayMethod method = request.getPayMethod();

        PaymentGatewayHandler gatewayHandler = gatewayHandlers.get(method);
        if (gatewayHandler == null) {
            throw new UnsupportedStrategyException("Unsupported payment gateway strategy: " + method);
        }

        return gatewayHandler.processPayment(userId, request);
    }

    @Transactional
    public ApiResponse<String> cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("booking not found"));

        booking.cancelBooking(userId);
        FacilityPackage facilityPackage = booking.getFacilityPackage();

        Long facilityPackageId = facilityPackage.getId();
        LocalDate usageDate = booking.getUsageDate();
        packageAvailabilityRepository.deleteByFacilityPackageIdAndTargetDate(facilityPackageId, usageDate);

        publisher.publishEvent(BookingMapper.toBookingCancelledEvent(booking));

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "예약이 취소되었습니다");
    }
}
