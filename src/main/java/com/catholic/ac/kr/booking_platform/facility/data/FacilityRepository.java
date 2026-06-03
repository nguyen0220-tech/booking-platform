package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilitySummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            JOIN FacilityRegistration fr ON fr.facility = f
            WHERE (
                LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(f.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(f.facilityType) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND f.active = true
            AND fr.status = :status
            """)
    Page<FacilitySummaryProjection> findByKeyword(
            @Param("keyword") String keyword,
            @Param("status") FacilityStatus status,
            Pageable pageable
    );
}
