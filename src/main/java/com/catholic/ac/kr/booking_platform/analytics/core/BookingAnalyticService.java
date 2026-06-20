package com.catholic.ac.kr.booking_platform.analytics.core;

import com.catholic.ac.kr.booking_platform.analytics.dto.AnalyticMapper;
import com.catholic.ac.kr.booking_platform.analytics.projection.BookingAnalyticProjection;
import com.catholic.ac.kr.booking_platform.analytics.dto.UserBookingAnalyticDTO;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingAnalyticService {

    private final BookingRepository bookingRepository;

    public UserBookingAnalyticDTO getUserBookingAnalytic(Long userId) {
        BookingAnalyticProjection projection = bookingRepository.getPersonalAnalyticsRaw(userId);

        return AnalyticMapper.convertToUserBookingAnalyticDTO(projection);
    }

}
