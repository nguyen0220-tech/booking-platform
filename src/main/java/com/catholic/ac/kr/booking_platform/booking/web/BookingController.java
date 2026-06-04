package com.catholic.ac.kr.booking_platform.booking.web;

import com.catholic.ac.kr.booking_platform.booking.core.BookingService;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiResponse<String> createBooking(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid BookingRequest bookingRequest){
        return bookingService.createBooking(userDetails.getId(), bookingRequest);
    }
}
