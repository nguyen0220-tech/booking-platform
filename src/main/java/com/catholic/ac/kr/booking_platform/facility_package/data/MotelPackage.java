package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.facility.data.Motel;
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
}
