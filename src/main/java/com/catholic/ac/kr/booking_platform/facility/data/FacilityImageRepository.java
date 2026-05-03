package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.dto.FacilityImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {
    @Query("""
            SELECT new com.catholic.ac.kr.booking_platform.facility.dto.FacilityImageDTO(fi.entityId,fi.imageUrl)
            FROM FacilityImage fi
            WHERE fi.entityId IN :entityIds
            """)
    List<FacilityImageDTO> findAllByEntityIdIdIn(@Param("entityIds") List<Long> entityIds);
}
