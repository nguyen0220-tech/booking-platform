package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.facility_package.dto.MotelPackageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotelPackageRepository extends JpaRepository<MotelPackage, Long> {
    @Query("""
            SELECT new com.catholic.ac.kr.booking_platform.facility_package.dto.MotelPackageDTO(
                        mp.id, mp.pricingType, mp.checkIn, mp.checkOut)
            FROM MotelPackage mp WHERE mp.id IN :ids
            """)
    List<MotelPackageDTO> findAllByIds(@Param("ids") List<Long> ids);
}
