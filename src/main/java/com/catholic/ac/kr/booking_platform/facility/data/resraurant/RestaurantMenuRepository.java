package com.catholic.ac.kr.booking_platform.facility.data.resraurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
    @Query("""
            SELECT rm FROM RestaurantMenu rm
            WHERE rm.restaurant.id IN :restaurantIds
            """)
    List<RestaurantMenu> findAllByRestaurantIds(List<Long> restaurantIds);
}
