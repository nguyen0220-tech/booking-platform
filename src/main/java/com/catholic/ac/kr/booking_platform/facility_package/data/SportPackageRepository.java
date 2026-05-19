package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.facility_package.projection.SportPackageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface SportPackageRepository extends JpaRepository<SportPackage, Long> {

    @Query("""
            SELECT sp.id AS id, sp.sport.id AS facilityId, sp.sport.owner.id AS ownerId,
                   sp.startTime AS startTime, sp.endTime AS endTime, sp.totalPrice AS totalPrice
            FROM SportPackage sp
            WHERE sp.sport.id = :sportId
            """)
    Page<SportPackageProjection> findBySportId(@Param("sportId") Long sportId, Pageable pageable);

    @Query("SELECT COUNT(sp) > 0 FROM SportPackage sp " +
            "WHERE sp.sport.id = :sportId " +
            "AND sp.startTime < :newEndTime " +
            "AND sp.endTime > :newStartTime")
    boolean existsOverlappingSlot(
            @Param("sportId") Long sportId,
            @Param("newStartTime") LocalTime newStartTime,
            @Param("newEndTime") LocalTime newEndTime
    );
}
