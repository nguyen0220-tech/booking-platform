package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityAdminProjection;
import com.catholic.ac.kr.booking_platform.facility.projection.FacilityRegistrationProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRegistrationRepository extends JpaRepository<FacilityRegistration, Long> {
    @Query("SELECT fr FROM FacilityRegistration fr WHERE fr.facility.id IN :facilityIds")
    List<FacilityRegistration> findAllByFacilityIdIn(@Param("facilityIds") List<Long> facilityIds);

    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId,fr.id AS facilityRegistrationId
            FROM FacilityRegistration fr
            JOIN Facility f ON fr.facility = f
            WHERE fr.status = :status
            """)
    Page<FacilityAdminProjection> findFacilityRegistrationByStatus(@Param("status") FacilityStatus status, Pageable pageable);

    @Query("""
            SELECT fr.id AS id, f.id AS facilityId,
                   f.facilityType AS facilityType,
                   fr.status AS status, fr.note AS note,
                   f.owner.id AS ownerId, fr.reviewer.id AS reviewerId, fr.lastUpdatedAt AS lastUpdateAt
            FROM FacilityRegistration fr
            JOIN Facility f on fr.facility = f
            WHERE fr.id = :id
            """)
    Optional<FacilityRegistrationProjection> findFacilityRegistrationById(Long id);
}
