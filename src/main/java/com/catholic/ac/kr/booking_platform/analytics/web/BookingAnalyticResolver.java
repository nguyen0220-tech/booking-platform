package com.catholic.ac.kr.booking_platform.analytics.web;

import com.catholic.ac.kr.booking_platform.analytics.core.BookingAnalyticService;
import com.catholic.ac.kr.booking_platform.analytics.dto.UserBookingAnalyticDTO;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookingAnalyticResolver {

    private final BookingAnalyticService bookingAnalyticService;

    @QueryMapping
    public UserBookingAnalyticDTO bookingAnalytic(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return  bookingAnalyticService.getUserBookingAnalytic(userDetails.getId());
    }

}
