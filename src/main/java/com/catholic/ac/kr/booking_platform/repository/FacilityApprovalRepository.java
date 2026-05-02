package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.enumdef.FacilityStatus;
import com.catholic.ac.kr.booking_platform.model.FacilityApproval;
import com.catholic.ac.kr.booking_platform.projection.FacilityProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityApprovalRepository extends JpaRepository<FacilityApproval, Long> {
    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM FacilityApproval fa
            JOIN Facility f ON fa.facility = f
            WHERE fa.status = :status
            """)
    Page<FacilityProjection> findFacilityApprovalByStatus(@Param("status") FacilityStatus status, Pageable pageable);
}
