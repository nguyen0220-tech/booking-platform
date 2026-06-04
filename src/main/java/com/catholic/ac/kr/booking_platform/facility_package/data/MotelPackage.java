package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import com.catholic.ac.kr.booking_platform.facility_package.constant.PricingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(
        name = "motel_package",
        indexes = @Index(columnList = "id")
)
@DiscriminatorValue("MOTEL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MotelPackage extends FacilityPackage {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PricingType pricingType;

    @Column(nullable = false)
    private LocalTime checkIn;

    @Column(nullable = false)
    private LocalTime checkOut;

    @Override
    public LocalTime getPackageStartTime() {
        return this.checkIn;
    }

    @Override
    public LocalTime getPackageEndTime() {
        return this.checkOut;
    }

    @Override
    public void applyTimeToBooking(Booking booking, LocalTime requestStartTime) {
        booking.setStartTime(this.getCheckIn());
        booking.setEndTime(this.getCheckOut());
    }
}
