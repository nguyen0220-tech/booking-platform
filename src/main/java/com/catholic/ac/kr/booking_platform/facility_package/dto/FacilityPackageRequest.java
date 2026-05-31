package com.catholic.ac.kr.booking_platform.facility_package.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "facilityType",
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
    @NotNull(message = "입력 필수 항목입니다")
    private Long facilityId;

    @NotBlank(message = "입력 필수 항목입니다")
    private String packageName;

    private String note;

    @NotNull(message = "입력 필수 항목입니다")
    private FacilityType facilityType;

    @NotNull(message = "입력 필수 항목입니다")
    private BigDecimal salePrice;
}
