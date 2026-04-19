package com.catholic.ac.kr.booking_platform.mapper;

import com.catholic.ac.kr.booking_platform.dto.ProfileDTO;
import com.catholic.ac.kr.booking_platform.dto.UserDTO;
import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.projection.UserProjection;


public class UserMapper {
    public static ProfileDTO userToUserDTO(User user) {
        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setFullName(user.getFullName());
        profileDTO.setEmail(HelperUtils.encodeEmail(user.getEmail()));
        profileDTO.setPhone(HelperUtils.encodePhone(user.getPhone()));
        profileDTO.setAvatarUrl(user.getAvatarUrl());

        return profileDTO;
    }

    public static UserDTO userDTO(UserProjection projection) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(projection.getId());

        return userDTO;
    }
}
