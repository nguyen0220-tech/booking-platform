package com.catholic.ac.kr.booking_platform.dto;

import com.catholic.ac.kr.booking_platform.enumdef.FoodType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDTO {
    private Long id;
    private FoodType foodType;
}
