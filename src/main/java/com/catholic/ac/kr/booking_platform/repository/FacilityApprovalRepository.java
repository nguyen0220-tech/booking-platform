package com.catholic.ac.kr.booking_platform.repository;

import com.catholic.ac.kr.booking_platform.model.FacilityApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityApprovalRepository extends JpaRepository<FacilityApproval, Long> {
}
