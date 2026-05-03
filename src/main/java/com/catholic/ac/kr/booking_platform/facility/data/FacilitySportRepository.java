package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.dto.SportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilitySportRepository extends JpaRepository<Sport, Long> {
    @Query("""
                SELECT new com.catholic.ac.kr.booking_platform.facility.dto.SportDTO(s.id, s.hourPrice)
                FROM Sport s
                WHERE s.id IN :ids
            """)
    List<SportDTO> findSportDTOsByIds(@Param("ids") List<Long> ids);
}