package com.catholic.ac.kr.booking_platform.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBookingAnalyticDTO {
    private int totalBookings;
    private int totalMonthlyBookings;
}
