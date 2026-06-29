package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityStatus;
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
            SELECT fr
            FROM FacilityRegistration fr
            JOIN Facility f ON fr.facility = f
            WHERE fr.status = :status
            """)
    Page<FacilityRegistration> findFacilityRegistrationByStatus(@Param("status") FacilityStatus status, Pageable pageable);

    @Query("""
            SELECT fr
            FROM FacilityRegistration fr
            JOIN Facility f ON fr.facility = f
            WHERE fr.id = :id
            """)
    Optional<FacilityRegistration> findFacilityRegistrationById(Long id);

    @Query("SELECT fr FROM FacilityRegistration fr " +
            "JOIN FETCH fr.facility f " +
            "WHERE fr.facility.id = :facilityId")
    Optional<FacilityRegistration> findByFacilityIdWithFacility(@Param("facilityId") Long facilityId);

    @Query("SELECT fr FROM FacilityRegistration fr " +
            "JOIN FETCH fr.facility f " +
            "JOIN FETCH f.owner o " +
            "WHERE f.id = :facilityId")
    Optional<FacilityRegistration> findByFacilityIdWithFacilityWithOwner(@Param("facilityId") Long facilityId);
}
