package com.catholic.ac.kr.booking_platform.review.core;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingDTO;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.AlreadyExistsException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.review.constant.ReviewStatus;
import com.catholic.ac.kr.booking_platform.review.core.event.NewReviewEvent;
import com.catholic.ac.kr.booking_platform.review.data.Review;
import com.catholic.ac.kr.booking_platform.review.data.ReviewRepository;
import com.catholic.ac.kr.booking_platform.review.dto.*;
import com.catholic.ac.kr.booking_platform.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ApplicationEventPublisher publisher;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public ListResponse<ReviewDTO> getAllByFacilityId(Long facilityId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Review> reviewPage = reviewRepository.findAllByFacilityId(facilityId, pageable);

        List<ReviewDTO> result = reviewPage.map(ReviewMapper::toReviewDTO).getContent();

        return new ListResponse<>(
                result,
                new PageInfo(page, size, reviewPage.hasNext(), reviewPage.getTotalElements(), reviewPage.getTotalPages())
        );
    }

    @Transactional
    public ApiResponse<String> createReview(Long userId, ReviewRequest request) {
        if (existingReview(request.getBookingId())) {
            throw new AlreadyExistsException("이미 리뷰를 작성하셨습니다");
        }
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("booking not found"));

        booking.validateReviewEligibility(userId);

        User reviewer = booking.getUser();

        Review review = new Review();
        review.setReviewer(reviewer);
        review.setBooking(booking);
        review.setFacilityId(booking.getFacilityId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());

        reviewRepository.save(review);
        publisher.publishEvent(new NewReviewEvent(booking.getFacilityId(), request.getRating()));

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "소중한 시간을 내 리뷰를 작성하셔서 갑사합니다.");
    }

    private boolean existingReview(Long bookingId) {
        return reviewRepository.existsByBookingId(bookingId);
    }

    public List<RatingGroupByProjection> getReviewGroupByDTO(List<Long> facilityIds) {

        return reviewRepository.groupByFacilityIdAndRating(facilityIds);
    }

    public Map<Long, ReviewEligibility> reviewBatchLoader(List<BookingDTO> bookings) {
        List<Long> bookingIds = bookings.stream()
                .map(BookingDTO::getId)
                .toList();

        Set<Long> reviewedBookingIds = reviewRepository.findReviewedBookingIds(bookingIds);

        return bookings.stream().collect(Collectors.toMap(
                BookingDTO::getId,
                booking -> {
                    boolean hasReviewed = reviewedBookingIds.contains(booking.getId());
                    return checkReviewEligibility(booking, hasReviewed);
                }
        ));
    }

    private ReviewEligibility checkReviewEligibility(BookingDTO booking, boolean hasReviewed) {
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return new ReviewEligibility(ReviewStatus.BOOKING_CANCELLED);
        }
        if (hasReviewed) {
            return new ReviewEligibility(ReviewStatus.ALREADY_REVIEWED);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDateTime = booking.getUsageDate()
                .atTime(booking.getEndTime() != null ? booking.getEndTime() : LocalTime.MAX);

        if (now.isBefore(endDateTime)) {
            return new ReviewEligibility(ReviewStatus.NOT_YET_COMPLETED);
        }
        if (now.isAfter(endDateTime.plusDays(3))) {
            return new ReviewEligibility(ReviewStatus.EXPIRED);
        }

        return new ReviewEligibility(ReviewStatus.ELIGIBLE);
    }
}
