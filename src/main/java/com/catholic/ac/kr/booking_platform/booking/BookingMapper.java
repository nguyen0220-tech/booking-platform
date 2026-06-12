package com.catholic.ac.kr.booking_platform.booking;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.booking.data.BookingDTO;

public class BookingMapper {
    public static BookingDTO toBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setUserId(booking.getUser().getId());
        bookingDTO.setFacilityPackageId(booking.getFacilityPackage().getId());
        Long owner = booking.getFacilityPackage().getFacility().getOwner().getId();
        bookingDTO.setFacilityPackageOwnerId(owner);
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
}
