package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.booking.data.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sport_package",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id", "start_time", "end_time"})
        }
)
@DiscriminatorValue("SPORT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SportPackage extends FacilityPackage {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Override
    public void applyTimeToBooking(Booking booking, LocalDate requestUsageDate, LocalTime requestStartTime) {
        LocalTime actualStartTime = this.startTime;
        booking.validateBookingTime(requestUsageDate, actualStartTime);

        booking.setStartTime(actualStartTime);
        booking.setEndTime(this.getEndTime());
    }
}
