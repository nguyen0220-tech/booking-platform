package com.catholic.ac.kr.booking_platform.facility_package.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface SportPackageRepository extends JpaRepository<SportPackage, Long> {
// .   @Query("""
//            SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END
//            FROM SportPackage sp
//            WHERE sp.startTime < :endTime AND sp.endTime > :startTime
//            """)
//    boolean existingPackage(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
