package com.catholic.ac.kr.booking_platform.mapper;

import com.catholic.ac.kr.booking_platform.dto.UserDTO;
import com.catholic.ac.kr.booking_platform.projection.UserProjection;


public class UserMapper {


    public static UserDTO userDTO(UserProjection projection) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(projection.getId());

        return userDTO;
    }
}
