package com.catholic.ac.kr.booking_platform.facility.dto;

import com.catholic.ac.kr.booking_platform.facility.constant.FacilityType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, //dùng một cái tên định danh để phân biệt
        include = JsonTypeInfo.As.EXISTING_PROPERTY, //khai báo tên định danh đó nằm ngay bên trong dữ liệu JSON mà người dùng gửi lên
        property = "type", // Thuộc tính Frontend gửi lên để phân biệt
        visible = true //Sau khi đọc xong trường type để chọn class -> gán giá trị đó vào biến private FacilityType type trong Java luôn
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SportRequest.class, name = "SPORT"),
        @JsonSubTypes.Type(value = MotelRequest.class, name = "MOTEL"),
        @JsonSubTypes.Type(value = RestaurantRequest.class, name = "RESTAURANT")
})
@Getter
@Setter
public abstract class FacilityRequest {
    @NotBlank(message = "입력 필수 항목입니다")
    private String name;

    @NotNull(message = "입력 필수 항목입니다")
    private FacilityType type;

    @NotBlank(message = "입력 필수 항목입니다")
    private String description;

    private boolean active;

    private boolean carPark;

    private boolean hasWifi;

    @NotBlank(message = "입력 필수 항목입니다")
    private String address;

    private String instruction;

    private List<String> images = new ArrayList<>();

}


