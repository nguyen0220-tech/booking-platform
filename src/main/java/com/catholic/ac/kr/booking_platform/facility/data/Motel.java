package com.catholic.ac.kr.booking_platform.facility.data;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("MOTEL")
@Getter @Setter
public class Motel extends Facility{
    @Column(nullable = false)
    private BigDecimal nightPrice;

    @Column(nullable = false)
    private  BigDecimal hourPrice;
}
