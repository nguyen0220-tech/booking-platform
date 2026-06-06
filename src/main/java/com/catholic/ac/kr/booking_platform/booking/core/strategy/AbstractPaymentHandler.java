package com.catholic.ac.kr.booking_platform.booking.core.strategy;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailability;
import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailabilityRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.AlreadyExistsException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class AbstractPaymentHandler implements PaymentGatewayHandler {
    protected final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FacilityPackageRepository packageRepository;
    private final PackageAvailabilityRepository packageAvailabilityRepository;

    public AbstractPaymentHandler(BookingRepository bookingRepository, UserRepository userRepository,
                                  FacilityPackageRepository packageRepository, PackageAvailabilityRepository packageAvailabilityRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.packageAvailabilityRepository = packageAvailabilityRepository;

    }

    protected void setBasisBooking(Booking booking, Long userId, BookingRequest request) {
        FacilityPackage facilityPackage = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        facilityPackage.validationPackage();

        Facility facility = facilityPackage.getFacility();
        facility.validateOperatingHours(request.getStartTime());

        PackageAvailability availability = getOrCreatePackageAvailability(
                request.getPackageId(), request.getUsageDate(), facilityPackage);

        if (!facilityPackage.getFacilityType().equals(FacilityType.RESTAURANT)) {
            if (availability.getBookedCount() >= 1) {
                throw new AlreadyExistsException("예약된 패키지입니다");
            }
            availability.setBookedCount(availability.getBookedCount() + 1);
            packageAvailabilityRepository.save(availability);
        } else {
            packageAvailabilityRepository.incrementBookedCount(request.getPackageId(), request.getUsageDate());
        }

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

    private PackageAvailability getOrCreatePackageAvailability(Long packageId, LocalDate targetDate, FacilityPackage facilityPackage) {
        try {
            return packageAvailabilityRepository.findByFacilityPackageIdAndTargetDate(packageId, targetDate)
                    .orElseGet(() -> {
                        PackageAvailability newAvailability = new PackageAvailability();
                        newAvailability.setFacilityPackage(facilityPackage);
                        newAvailability.setTargetDate(targetDate);
                        newAvailability.setBookedCount(0);
                        return packageAvailabilityRepository.saveAndFlush(newAvailability);
                    });
        } catch (DataIntegrityViolationException e) {
            return packageAvailabilityRepository.findByFacilityPackageIdAndTargetDate(packageId, targetDate)
                    .orElseThrow();
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
