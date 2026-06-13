package com.catholic.ac.kr.booking_platform.booking.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT b FROM Booking b " +
            "WHERE b.user.id = :userId",
            countQuery = "SELECT count(b) FROM Booking b WHERE b.user.id = :userId")
    Page<Booking> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b " +
            "WHERE b.facilityOwnerId = :facilityOwnerId")
    Page<Booking> findByFacilityOwnerId(@Param("facilityOwnerId") Long facilityOwnerId, Pageable pageable);
}
