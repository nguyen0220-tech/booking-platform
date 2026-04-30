package com.catholic.ac.kr.booking_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SPORT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sport extends Facility {
    @Column(nullable = false)
    private BigDecimal hourPrice;
}
