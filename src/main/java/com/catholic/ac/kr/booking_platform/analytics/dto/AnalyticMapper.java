package com.catholic.ac.kr.booking_platform.analytics.dto;

import com.catholic.ac.kr.booking_platform.analytics.projection.BookingAnalyticProjection;

public class AnalyticMapper {
    public static UserBookingAnalyticDTO convertToUserBookingAnalyticDTO(BookingAnalyticProjection projection) {
        UserBookingAnalyticDTO analyticDTO = new UserBookingAnalyticDTO();

        analyticDTO.setTotalBookings(projection.getTotalBookings());
        analyticDTO.setTotalMonthlyBookings(projection.getTotalMonthlyBookings());

        return analyticDTO;
    }
}
