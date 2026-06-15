package com.catholic.ac.kr.booking_platform.booking.core;

import com.catholic.ac.kr.booking_platform.booking.BookingMapper;
import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingDTO;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.SecurityUtils;
import com.catholic.ac.kr.booking_platform.user.constant.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class BookingQueryService {
    private final Map<RoleName, BiFunction<Long, Pageable, ListResponse<BookingDTO>>> queryHandlers = Map.of(
            RoleName.USER, this::getBookingsByUserId,
            RoleName.PROVIDER, this::getBookingsByFacilityOwnerId,
            RoleName.ADMIN, this::getAllBooking
    );
    private final BookingRepository bookingRepository;

    public BookingDTO getBookingById(Long currentUserId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("booking not found"));

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = SecurityUtils.isAdmin(principal);
        boolean isFacilityOwner = currentUserId.equals(booking.getFacilityOwnerId());

        if (!currentUserId.equals(booking.getUser().getId()) &&  !isAdmin && !isFacilityOwner) {
            throw new AccessDeniedException("본 예약의 해당자가 아닙니다");
        }

        return BookingMapper.toBookingDTO(booking);
    }

    public List<Booking> getAllByPackageIds( List<Long> packageIds) {
        return bookingRepository.findByFacilityPackageIdsAndStatus(packageIds, BookingStatus.PAID);
    }

    public ListResponse<BookingDTO> getBookingsWithRole(Long entityId, RoleName roleName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return queryHandlers.getOrDefault(roleName, this::unsupported).apply(entityId, pageable);
    }

    private ListResponse<BookingDTO> getBookingsByUserId(Long userId, Pageable pageable) {
        Page<Booking> bookingPage = bookingRepository.findByUserId(userId, pageable);

        return toListResponse(bookingPage, pageable);
    }

    private ListResponse<BookingDTO> getBookingsByFacilityOwnerId(Long facilityOwnerId, Pageable pageable) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        boolean isOwner = SecurityUtils.isProvider(principal);
        if (!isOwner) {
            throw new AccessDeniedException("제공자의 데이터입니다");
        }

        Page<Booking> bookingPage = bookingRepository
                .findByFacilityOwnerId(facilityOwnerId, pageable);

        return toListResponse(bookingPage, pageable);
    }

    private ListResponse<BookingDTO> getAllBooking(Long admin, Pageable pageable) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = SecurityUtils.isAdmin(principal);
        if (!isAdmin) {
            throw new AccessDeniedException("관리자의 데이터입니다");
        }

        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        return toListResponse(bookingPage, pageable);
    }

    private ListResponse<BookingDTO> unsupported(Long entityId, Pageable pageable) {
        throw new BadRequestException("지원하지 않는 타입입니다");
    }

    private ListResponse<BookingDTO> toListResponse(Page<Booking> bookingPage, Pageable pageable) {
        List<BookingDTO> bookings = bookingPage.map(BookingMapper::toBookingDTO).getContent();

        return new ListResponse<>(bookings,
                new PageInfo(pageable.getPageNumber(), pageable.getPageSize(),
                        bookingPage.hasNext(), bookingPage.getTotalElements(), bookingPage.getTotalPages()));
    }
}
