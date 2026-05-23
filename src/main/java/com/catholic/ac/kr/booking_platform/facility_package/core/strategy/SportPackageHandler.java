package com.catholic.ac.kr.booking_platform.facility_package.core.strategy;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility.data.FacilitySportRepository;
import com.catholic.ac.kr.booking_platform.facility.data.Sport;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackageRepository;
import com.catholic.ac.kr.booking_platform.facility_package.data.SportPackage;
import com.catholic.ac.kr.booking_platform.facility_package.dto.SportPackageRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
    public ApiResponse<String> createPackage(Long ownerId, Long sportId, SportPackageRequest request) {
        Sport sport = facilitySportRepository.findById(sportId)
                .orElseThrow(() -> new ResourceNotFoundException("sport not found"));

        validateFacility(ownerId, sport);

        SportPackage newPackage = new SportPackage();

        setBasicPackage(newPackage, request);

        newPackage.setFacility(sport);
        newPackage.setStartTime(request.getStartTime());
        newPackage.setEndTime(request.getEndTime());
        setPackagePrice(newPackage, sport);
        newPackage.setSalePrice(request.getSalePrice());

        packageRepository.save(newPackage);

        return buildResponseSuccess(request.getPackageName());
    }

    private void setPackagePrice(SportPackage sportPackage, Sport sport) {
        BigDecimal price = sport.getHourPrice();
        sportPackage.setSalePrice(price);
    }
}
