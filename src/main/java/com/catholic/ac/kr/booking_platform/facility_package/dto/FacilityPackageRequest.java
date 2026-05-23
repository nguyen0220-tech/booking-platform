package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "facility_type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SportPackageRequest.class, name = "SPORT"),
        @JsonSubTypes.Type(value = MotelPackageRequest.class, name = "MOTEL"),
        @JsonSubTypes.Type(value = RestaurantPackageRequest.class, name = "RESTAURANT")
})
@Getter
@Setter
public abstract class FacilityPackageRequest {
    @NotBlank(message = "입력 필수 항목입니다")
    private String packageName;

    private String note;

    @NotBlank(message = "입력 필수 항목입니다")
    private FacilityType facilityType;

    @NotBlank(message = "입력 필수 항목입니다")
    private BigDecimal salePrice;
}
