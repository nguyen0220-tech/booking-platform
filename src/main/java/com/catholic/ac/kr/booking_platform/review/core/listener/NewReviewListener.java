package com.catholic.ac.kr.booking_platform.review.core.listener;

import com.catholic.ac.kr.booking_platform.facility.data.Facility;
import com.catholic.ac.kr.booking_platform.facility.data.FacilityRepository;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.review.core.event.NewReviewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NewReviewListener {

    private final FacilityRepository facilityRepository;

    @Async
    @EventListener
    @Transactional
    public void handleNewReviewEvent(NewReviewEvent event) {
        Facility facility = facilityRepository.findById(event.facilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found"));

        int rating = event.rating().getValue();

        Double newAverage = facility.calculateRunningAverageRating(rating);

        facility.setAverageRating(newAverage);
        facility.setTotalReviews(facility.getTotalReviews() + 1);
    }
}
