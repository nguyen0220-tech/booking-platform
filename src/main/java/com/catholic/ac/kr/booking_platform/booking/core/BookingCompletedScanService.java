package com.catholic.ac.kr.booking_platform.booking.core;

import com.catholic.ac.kr.booking_platform.booking.constant.BookingStatus;
import com.catholic.ac.kr.booking_platform.booking.data.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingCompletedScanService {
    private final BookingRepository bookingRepository;

    @Scheduled(cron = "01 03 01 * * ?")
    public void processBookingCompletedScan() {
        LocalDate today = LocalDate.now();
        bookingRepository.completeBooking(BookingStatus.COMPLETED, today, BookingStatus.PAID);
    }
}
