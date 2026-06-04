package com.catholic.ac.kr.booking_platform.booking.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PackageAvailabilityRepository extends JpaRepository<PackageAvailability, Long> {
    Optional<PackageAvailability> findByFacilityPackageIdAndTargetDate(Long facilityPackageId, LocalDate targetDate);

    @Modifying
    @Query("UPDATE PackageAvailability p SET p.bookedCount = p.bookedCount + 1 " +
            "WHERE p.facilityPackage.id = :packageId AND p.targetDate = :targetDate")
    void incrementBookedCount(@Param("packageId") Long packageId, @Param("targetDate") LocalDate targetDate);

}
