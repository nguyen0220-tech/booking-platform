package com.catholic.ac.kr.booking_platform.review.data;

import com.catholic.ac.kr.booking_platform.review.dto.RatingGroupByProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookingId(Long bookingId);

    Page<Review> findAllByFacilityId(Long facilityId, Pageable pageable);

    @Query("""
            SELECT r.facilityId AS facilityId, r.rating AS rating, COUNT(r.rating) AS count
            FROM Review r
            WHERE r.facilityId IN :facilityIds
            GROUP BY r.facilityId, r.rating
            
            """)
    List<RatingGroupByProjection> groupByFacilityIdAndRating(List<Long> facilityIds);
}
