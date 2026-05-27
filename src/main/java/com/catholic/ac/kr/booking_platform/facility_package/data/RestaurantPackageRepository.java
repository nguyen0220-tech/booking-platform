package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.facility_package.dto.RestaurantPackageDTO;
import com.catholic.ac.kr.booking_platform.facility_package.dto.RestaurantPackageMenuDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantPackageRepository extends JpaRepository<RestaurantPackage, Long> {
    @Query("""
            SELECT new com.catholic.ac.kr.booking_platform.facility_package.dto.RestaurantPackageDTO(
                        rp.id, rp.maxCapacity)
            FROM RestaurantPackage rp WHERE rp.id IN :ids
            """)
    List<RestaurantPackageDTO> findAllByIds(@Param("ids") List<Long> ids);

    @Query("""
            SELECT new com.catholic.ac.kr.booking_platform.facility_package.dto.RestaurantPackageMenuDTO(
                       rp.id, m.name, m.description, m.price, m.imageUrl)
            FROM  RestaurantPackage rp
            JOIN rp.menus m
            WHERE rp.id IN :packageIds
            """)
    List<RestaurantPackageMenuDTO> findAllByPackageId(@Param("packageIds") List<Long> packageIds);
}
