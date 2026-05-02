package com.catholic.ac.kr.booking_platform.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FacilityInfoDTO {
    private String name;
    private String description;
    private String instruction;
    private String address;
    private boolean active;
    private boolean carPark;
    private boolean hasWifi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
