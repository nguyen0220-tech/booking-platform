package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.dto.SportDTO;
import com.catholic.ac.kr.booking_platform.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilitySportRepository extends JpaRepository<Sport, Long> {
    @Query("""
                SELECT new com.catholic.ac.kr.booking_platform.dto.SportDTO(s.id, s.hourPrice)
                FROM Sport s
                WHERE s.id IN :ids
            """)
    List<SportDTO> findSportDTOsByIds(@Param("ids") List<Long> ids);
}