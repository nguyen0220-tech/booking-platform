package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FacilityInfoSportUpdateUpdateRequest.class, name = "SPORT"),
        @JsonSubTypes.Type(value = FacilityInfoMotelUpdateUpdateRequest.class, name = "MOTEL"),
        @JsonSubTypes.Type(value = FacilityInfoRestaurantUpdateUpdateRequest.class, name = "RESTAURANT"),
})
@Getter
@Setter
public abstract class FacilityInfoUpdateRequest {
    private Long facilityId;
    private FacilityType type;

    private String name;
    private String address;
    private String description;
    private String instruction;
}