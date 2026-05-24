package com.catholic.ac.kr.booking_platform.facility_package.core.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityMotelRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Motel;
import com.catholic.ac.kr.booking_platform.facility_package.constant.PricingType;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.MotelPackage;
import com.catholic.ac.kr.booking_platform.facility_package.dto.MotelPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

@Component
public class MotelPackageHandler extends AbstractPackageHandler<MotelPackageRequest> {

    private final FacilityMotelRepository facilityMotelRepository;

    public MotelPackageHandler(FacilityPackageRepository packageRepository, FacilityMotelRepository facilityMotelRepository) {
        super(packageRepository);
        this.facilityMotelRepository = facilityMotelRepository;
    }

    @Override
    public FacilityType getFacilityType() {
        return FacilityType.MOTEL;
    }

    @Override
    public ApiResponse<String> processCreate(Long ownerId, Long motelId, MotelPackageRequest request) {
        Motel motel = facilityMotelRepository.findById(motelId)
                .orElseThrow(() -> new ResourceNotFoundException("motel not found"));

        validateFacility(ownerId, motel);

        BigDecimal basicPrice = motelPriceCalculator(
                motel, request.getPricingType(), request.getCheckIn(), request.getCheckOut());

        if (basicPrice.compareTo(request.getSalePrice()) < 0) {
            throw new BadRequestException("할인 가겨이 원가격(" + basicPrice + ")보다 큽니다");
        }

        MotelPackage motelPackage = new MotelPackage();

        setBasicPackage(motelPackage, request);

        motelPackage.setFacility(motel);
        motelPackage.setCheckIn(request.getCheckIn());
        motelPackage.setCheckOut(request.getCheckOut());
        motelPackage.setPrice(basicPrice);
        motelPackage.setSalePrice(request.getSalePrice());
        motelPackage.setPricingType(request.getPricingType());

        packageRepository.save(motelPackage);

        return buildResponseSuccess(request.getPackageName());
    }

    private BigDecimal motelPriceCalculator(Motel motel, PricingType type, LocalTime checkIn, LocalTime checkOut) {
        switch (type) {
            case HOURLY -> {
                BigDecimal hourPrice = motel.getHourPrice();

                Duration duration = Duration.between(checkIn, checkOut);
                BigDecimal totalHours = BigDecimal.valueOf(duration.toMinutes())
                        .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

                return hourPrice.multiply(totalHours);
            }

            case NIGHT -> {
                return motel.getNightPrice(); //1박 가격
            }

            default -> throw new IllegalArgumentException("Unsupported pricing type");
        }
    }
}
