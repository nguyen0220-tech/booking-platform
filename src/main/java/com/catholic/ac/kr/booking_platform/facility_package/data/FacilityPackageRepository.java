package com.catholic.ac.kr.booking_platform.facility_package.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityPackageRepository extends JpaRepository<FacilityPackage, Long> {
    @Query("""
            SELECT fp
            FROM FacilityPackage fp
            WHERE fp.facility.id = :facilityId
            """)
    Page<FacilityPackage> findByFacilityId(@Param("facilityId") Long facilityId, Pageable pageable);

    @Query("""
            SELECT fp
            FROM FacilityPackage fp
            WHERE fp.facility.id = :facilityId AND fp.active = :active
            """)
    Page<FacilityPackage> findByFacilityIdAndActive(Long facilityId, boolean active, Pageable pageable);
}
