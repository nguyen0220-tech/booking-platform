package com.catholic.ac.kr.booking_platform.booking.core.strategy;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.AlreadyExistsException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class AbstractPaymentHandler implements PaymentGatewayHandler {
    protected final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FacilityPackageRepository packageRepository;

    public AbstractPaymentHandler(BookingRepository bookingRepository, UserRepository userRepository, FacilityPackageRepository packageRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }

    protected void setBasisBooking(Booking booking, Long userId, Long packageId,
                                   LocalDate usageDate, LocalTime startTime) {

        if (existingBooking(packageId, usageDate)) {
            throw new AlreadyExistsException("Booking already exists");
        }

        FacilityPackage facilityPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        validatePackage(facilityPackage);

        booking.setUser(user);
        booking.setUsageDate(usageDate);
        facilityPackage.applyTimeToBooking(booking, startTime);
        booking.setFacilityPackage(facilityPackage);
        booking.setStatus(BookingStatus.PAID);

        BigDecimal packagePrice = facilityPackage.getSalePrice().compareTo(BigDecimal.ZERO) == 0 ?
                facilityPackage.getPrice() : facilityPackage.getSalePrice();
        booking.setAmount(applyAmount(packagePrice));
    }

    private boolean existingBooking(Long packageId, LocalDate bookingDate) {
        return bookingRepository.existsByFacilityPackageIdAndUsageDate(packageId, bookingDate);
    }

    private void validatePackage(FacilityPackage facilityPackage) {
        if (!facilityPackage.isActive()) {
            throw new BadRequestException("예약 가능한 상태가 아닙니다");
        }
    }

    private BigDecimal applyAmount(BigDecimal packageAmount) {
        return packageAmount.multiply(BigDecimal.valueOf(1 - discountWithPayMethod()));
    }

    protected abstract double discountWithPayMethod();

    protected ApiResponse<String> buildResponseSuccess(PayMethod method) {
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "예약이 완료되었습니다 (" + method.name() + ")");
    }
}
