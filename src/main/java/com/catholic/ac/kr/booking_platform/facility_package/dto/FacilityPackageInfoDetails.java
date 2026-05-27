package com.catholic.ac.kr.booking_platform.facility_package.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FacilityPackageInfoDetails {
    private String packageName;
    private String note;
    private int totalCount;
    private BigDecimal price;
    private BigDecimal salePrice;
    private boolean active;
}
