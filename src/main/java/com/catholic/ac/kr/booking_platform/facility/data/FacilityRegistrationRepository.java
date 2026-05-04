package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRegistrationRepository extends JpaRepository<FacilityRegistration, Long> {
    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM FacilityRegistration fg
            JOIN Facility f ON fg.facility = f
            WHERE fg.status = :status
            """)
    Page<FacilityProjection> findFacilityApprovalByStatus(@Param("status") FacilityStatus status, Pageable pageable);
}
