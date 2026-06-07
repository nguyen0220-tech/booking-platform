package com.catholic.ac.kr.booking_platform.booking.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            SELECT b
            FROM Booking b WHERE b.facilityPackage.id IN :facilityPackageId
            """)
    List<Booking> findByFacilityPackageIds(@Param("facilityPackageId") List<Long> facilityPackageId);
}
