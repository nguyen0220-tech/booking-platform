package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.model.Facility;
import com.catholic.ac.kr.booking_platform.projection.FacilityProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    @Query("""
            SELECT f.id AS id, f.facilityType AS facilityType, f.owner.id AS ownerId
            FROM Facility f WHERE f.owner.id = :ownerId
            """)
    Page<FacilityProjection> findByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);


}
