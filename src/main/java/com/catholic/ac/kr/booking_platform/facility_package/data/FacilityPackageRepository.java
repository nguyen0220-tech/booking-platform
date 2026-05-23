package com.catholic.ac.kr.booking_platform.facility_package.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityPackageRepository extends JpaRepository<FacilityPackage, Long> {
}
