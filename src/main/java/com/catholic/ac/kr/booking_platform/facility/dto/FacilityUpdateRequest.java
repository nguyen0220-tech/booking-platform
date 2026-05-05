package com.catholic.ac.kr.booking_platform.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FacilityUpdateRequest {
    private String name;
    private String description;
    private boolean active;
    private boolean carPark;
    private boolean hasWifi;
    private String address;
    private String instruction;
    private List<String> images = new ArrayList<>();

}
