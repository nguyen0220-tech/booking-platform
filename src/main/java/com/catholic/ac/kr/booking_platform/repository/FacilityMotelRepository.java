package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.dto.MotelDTO;
import com.catholic.ac.kr.booking_platform.dto.SportDTO;
import com.catholic.ac.kr.booking_platform.model.Motel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityMotelRepository extends JpaRepository<Motel, Long> {
    @Query("""
                SELECT new com.catholic.ac.kr.booking_platform.dto.MotelDTO(m.id,m.nightPrice, m.hourPrice)
                FROM Motel m
                WHERE m.id IN :ids
            """)
    List<MotelDTO> findMotelDTOsByIds(@Param("ids") List<Long> ids);
}
