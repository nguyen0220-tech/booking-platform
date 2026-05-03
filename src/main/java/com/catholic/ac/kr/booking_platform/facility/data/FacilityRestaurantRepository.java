package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.dto.RestaurantDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("""
                SELECT new com.catholic.ac.kr.booking_platform.facility.dto.RestaurantDTO(r.id, r.foodType)
                FROM Restaurant r
                WHERE r.id IN :ids
            """)
    List<RestaurantDTO> findRestaurantDTOsByIds(@Param("ids") List<Long> ids);
}
