package com.catholic.ac.kr.booking_platform.helper;

import com.catholic.ac.kr.booking_platform.dto.RoleForBatchDTO;
import com.catholic.ac.kr.booking_platform.dto.UserInfoDetailsDTO;
import com.catholic.ac.kr.booking_platform.model.User;

public class ConverterForBatchMapping {
    public static UserInfoDetailsDTO convertToUserInFfo(User user) {
        UserInfoDetailsDTO userInfoDetailsDTO = new UserInfoDetailsDTO();

        userInfoDetailsDTO.setUsername(user.getUsername());
        userInfoDetailsDTO.setFullName(user.getFullName());
        userInfoDetailsDTO.setPhone(user.getPhone());
        userInfoDetailsDTO.setEmail(user.getEmail());
        userInfoDetailsDTO.setAvatarUrl(user.getAvatarUrl());
        userInfoDetailsDTO.setEnabled(user.isEnabled());
        userInfoDetailsDTO.setBlocked(user.isBlocked());
        userInfoDetailsDTO.setCreatedAt(user.getCreatedAt());

        return userInfoDetailsDTO;
    }

    public static String convertToRoleName(RoleForBatchDTO role) {
        return role.getName().toString();
    }

}
