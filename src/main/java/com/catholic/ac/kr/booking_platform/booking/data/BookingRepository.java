package com.catholic.ac.kr.booking_platform.booking.data;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            SELECT b
            FROM Booking b WHERE b.facilityPackage.id IN :facilityPackageId AND b.status = :status
            """)
    List<Booking> findByFacilityPackageIdsAndStatus(
            @Param("facilityPackageId") List<Long> facilityPackageId, @Param("status") BookingStatus status);

    @Query(value = "SELECT b FROM Booking b " +
            "WHERE b.user.id = :userId",
            countQuery = "SELECT count(b) FROM Booking b WHERE b.user.id = :userId")
    Page<Booking> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b " +
            "WHERE b.facilityOwnerId = :facilityOwnerId")
    Page<Booking> findByFacilityOwnerId(@Param("facilityOwnerId") Long facilityOwnerId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Booking b
            SET b.status = :targetStatus
            WHERE b.usageDate < :today AND b.status = :bookingStatus
            """)
    void completeBooking(
            @Param("targetStatus") BookingStatus targetStatus,
            @Param("today") LocalDate today,
            @Param("bookingStatus") BookingStatus bookingStatus);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.user.id = :userId AND b.usageDate <= :threeDaysLater
            AND b.usageDate >= :today AND b.status = :status
            """)
    List<Booking> findAllWithinNext3Days(@Param("userId") Long userId, @Param("threeDaysLater") LocalDate threeDaysLater,
                                         @Param("today") LocalDate today, @Param("status") BookingStatus status);
}
