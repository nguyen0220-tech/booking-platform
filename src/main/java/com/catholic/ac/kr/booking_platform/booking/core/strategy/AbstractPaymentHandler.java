package com.catholic.ac.kr.booking_platform.booking.core.strategy;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.core.PackageAvailabilityService;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class AbstractPaymentHandler implements PaymentGatewayHandler {
    protected final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FacilityPackageRepository packageRepository;
    private final PackageAvailabilityService packageAvailabilityService;

    protected void setBasisBooking(Booking booking, Long userId, BookingRequest request) {
        booking.validateBookingTime(request.getUsageDate(), request.getStartTime());

        FacilityPackage facilityPackage = packageRepository.findByIdWithFacility(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        facilityPackage.validatePackage();

        Facility facility = facilityPackage.getFacility();
        facility.validateFacility();
        facility.validateOperatingHours(request.getStartTime()); //only RESTAURANT

        packageAvailabilityService.reserveSlot(facilityPackage, request.getUsageDate());

        //trả về một object giả (Proxy) chỉ chứa ID (name, email...trống rỗng.)
        User user = userRepository.getReferenceById(userId);

        booking.setUser(user);
        booking.setUsageDate(request.getUsageDate());
        facilityPackage.applyTimeToBooking(booking, request.getStartTime());
        booking.setFacilityPackage(facilityPackage);
        booking.setStatus(BookingStatus.PAID);

        BigDecimal packagePrice = facilityPackage.getSalePrice().compareTo(BigDecimal.ZERO) == 0 ?
                facilityPackage.getPrice() : facilityPackage.getSalePrice();
        booking.setAmount(applyAmount(packagePrice));
        booking.setBasisPrice(facilityPackage.getPrice());

        facilityPackage.setTotalCount(facilityPackage.getTotalCount() + 1);
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
