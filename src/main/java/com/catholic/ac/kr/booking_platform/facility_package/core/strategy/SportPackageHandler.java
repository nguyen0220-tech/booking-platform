package com.catholic.ac.kr.booking_platform.facility_package.core.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.FacilitySportRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Sport;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.SportPackage;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

@Component
public class SportPackageHandler extends AbstractPackageHandler<SportPackageRequest> {
    private final FacilitySportRepository facilitySportRepository;

    public SportPackageHandler(
            FacilityPackageRepository packageRepository,
            FacilitySportRepository facilitySportRepository) {

        super(packageRepository);
        this.facilitySportRepository = facilitySportRepository;
    }

    @Override
    public FacilityType getFacilityType() {
        return FacilityType.SPORT;
    }

    @Override
    public ApiResponse<String> processCreate(Long ownerId, Long sportId, SportPackageRequest request) {
        Sport sport = facilitySportRepository.findById(sportId)
                .orElseThrow(() -> new ResourceNotFoundException("sport not found"));

        validateFacility(ownerId, sport);

        BigDecimal basicPrice = sportPriceCalculator(
                sport.getHourPrice(), request.getStartTime(), request.getEndTime());
        if (basicPrice.compareTo(request.getSalePrice()) < 0) {
            throw new BadRequestException("할인 가겨이 원가격(" + basicPrice + ")보다 큽니다");
        }

        SportPackage sportPackage = new SportPackage();

        setBasicPackage(sportPackage, request);

        sportPackage.setFacility(sport);
        sportPackage.setStartTime(request.getStartTime());
        sportPackage.setEndTime(request.getEndTime());
        sportPackage.setPrice(basicPrice);
        sportPackage.setSalePrice(request.getSalePrice());

        packageRepository.save(sportPackage);

        return buildResponseSuccess(request.getPackageName());
    }

    private BigDecimal sportPriceCalculator(BigDecimal hourPrice, LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);

        BigDecimal totalHours = BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        return hourPrice.multiply(totalHours);
    }
}
