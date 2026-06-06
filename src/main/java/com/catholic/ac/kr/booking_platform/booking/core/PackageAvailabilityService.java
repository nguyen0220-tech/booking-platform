package com.catholic.ac.kr.booking_platform.booking.core;

import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailability;
import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailabilityRepository;
import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.catholic.ac.kr.booking_platform.facility_package.data.FacilityPackage;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PackageAvailabilityService {
    private final PackageAvailabilityRepository packageAvailabilityRepository;

    @Transactional
    public void reserveSlot(FacilityPackage facilityPackage, LocalDate targetDate) {
        PackageAvailability availability = getOrCreatePackageAvailability(
                facilityPackage.getId(), targetDate, facilityPackage);

        if (!facilityPackage.getFacilityType().equals(FacilityType.RESTAURANT)) {
            if (availability.getBookedCount() >= 1) {
                throw new AlreadyExistsException("예약된 패키지입니다");
            }
            availability.setBookedCount(availability.getBookedCount() + 1);
            packageAvailabilityRepository.save(availability); // Kích hoạt Khóa lạc quan (@Version)
        } else {
            // Update trực tiếp xuống DB (Atomic update) cho nhà hàng tránh nghẽn
            packageAvailabilityRepository.incrementBookedCount(facilityPackage.getId(), targetDate);
        }
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
}