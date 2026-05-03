package com.catholic.ac.kr.booking_platform.facility.data;

import com.catholic.ac.kr.booking_platform.facility.constant.FoodType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("RESTAURANT")
@Getter @Setter
public class Restaurant extends Facility{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType; //식당 종류: 한식, 일식, 중식...
}
