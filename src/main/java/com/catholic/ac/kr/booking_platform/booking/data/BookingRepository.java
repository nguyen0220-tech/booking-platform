package com.catholic.ac.kr.booking_platform.booking.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByFacilityPackageIdAndUsageDate(Long facilityPackageId, LocalDate usageDate);
}
