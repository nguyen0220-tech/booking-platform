package com.catholic.ac.kr.booking_platform.booking.core.strategy;

import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailabilityRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApplePayment extends AbstractPaymentHandler{
    public ApplePayment(BookingRepository bookingRepository, UserRepository userRepository,
                        FacilityPackageRepository packageRepository, PackageAvailabilityRepository availabilityRepository) {
        super(bookingRepository, userRepository, packageRepository, availabilityRepository);
    }

    @Override
    public PayMethod getPayMethod() {
        return PayMethod.APPLE_PAY;
    }

    @Override
    @Transactional
    public ApiResponse<String> processPayment(Long userId, BookingRequest request) {
        Booking booking = new Booking();

        setBasisBooking(booking, userId, request);
        booking.setPayMethod(getPayMethod());

        bookingRepository.save(booking);

        return buildResponseSuccess(getPayMethod());
    }

    @Override
    public double discountWithPayMethod() {
        return 0.2;
    }
}
