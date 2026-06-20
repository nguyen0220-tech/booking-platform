package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.projection.FacilitySummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("""
            SELECT f.owner.id
            FROM Facility f WHERE f.id = :id
            """)
    Optional<Long> findOwnerIdByFacilityId(@Param("id") Long id);

    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM Facility f WHERE f.id = :id
            """)
    Optional<FacilitySummaryProjection> findFacilityById(@Param("id") Long id);

    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM Facility f WHERE f.owner.id = :ownerId
            """)
    Page<FacilitySummaryProjection> findByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM Facility f WHERE f.owner.id = :ownerId AND f.name LIKE CONCAT('%', :keyword, '%')
            """)
    Page<FacilitySummaryProjection> findByOwnerIdAndKeyword(
            @Param("ownerId") Long ownerId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM Facility f
            WHERE (
                LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(f.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(f.facilityType) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND f.active = true
            """)
    Page<FacilitySummaryProjection> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
            ( -- Query 1: recently
                SELECT f.id, f.facility_type, f.owner_id, 1 as priority
                FROM facility f JOIN bookings b ON f.id = b.facility_id
                WHERE b.user_id = :userId AND b.usage_date < :today AND b.status = :status AND f.is_suspended = false
                GROUP BY f.id, f.facility_type, f.owner_id
                ORDER BY MAX(b.usage_date) DESC LIMIT 3
            )
            UNION ALL
            ( -- Query 2: selling
                SELECT f.id, f.facility_type, f.owner_id, 2 as priority
                FROM facility f JOIN facility_package fp ON f.id = fp.facility_id
                WHERE f.is_suspended = false
                GROUP BY f.id, f.facility_type, f.owner_id
                ORDER BY MAX(fp.total_count) DESC LIMIT 3
            )
            ORDER BY priority;
            """, nativeQuery = true)
    List<FacilitySummaryProjection> findFacilitiesRecentlyAndTopSelling(
            @Param("userId") Long userId,
            @Param("today") LocalDate today,
            @Param("status") String status);

    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM Facility f
            WHERE f.active = true
              AND LOWER(f.address) LIKE LOWER(CONCAT('%', :address, '%'))
            """)
    Page<FacilitySummaryProjection> findAllByAddress(String address, Pageable pageable);
}
