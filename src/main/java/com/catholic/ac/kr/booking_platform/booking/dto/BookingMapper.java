package com.catholic.ac.kr.booking_platform.booking.dto;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.core.event.BookingCancelledEvent;

import java.time.LocalDate;

public class BookingMapper {
    public static BookingDTO toBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setUserId(booking.getUser().getId());
        bookingDTO.setFacilityPackageId(booking.getFacilityPackage().getId());
        bookingDTO.setFacilityOwnerId(booking.getFacilityOwnerId());
        bookingDTO.setFacilityId(booking.getFacilityId());
        bookingDTO.setAmount(booking.getAmount());
        bookingDTO.setBasisPrice(booking.getBasisPrice());
        bookingDTO.setUsageDate(booking.getUsageDate());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setPayMethod(booking.getPayMethod());
        bookingDTO.setCreatedAt(booking.getCreatedAt());

        return bookingDTO;
    }

    public static BookingCancelledEvent toBookingCancelledEvent(Booking booking) {
        BookingCancelledEvent event = new BookingCancelledEvent();

        event.setUserId(booking.getUser().getId());
        event.setAmount(booking.getAmount());
        event.setUsageDate(booking.getUsageDate());
        event.setCancelDate(LocalDate.now());

        return event;
    }
}
