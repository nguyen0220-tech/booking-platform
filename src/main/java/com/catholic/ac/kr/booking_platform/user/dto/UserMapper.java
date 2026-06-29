package com.catholic.ac.kr.booking_platform.user.dto;

import com.catholic.ac.kr.booking_platform.profile.data.ProfileDTO;
import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.user.data.User;


public class UserMapper {
    public static ProfileDTO userToUserDTO(User user) {
        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setFullName(user.getFullName());
        profileDTO.setEmail(HelperUtils.encodeEmail(user.getEmail()));
        profileDTO.setPhone(HelperUtils.encodePhone(user.getPhone()));
        profileDTO.setAvatarUrl(user.getAvatarUrl());

        return profileDTO;
    }

    public static UserDTO toUserDTO(User entity) {
        UserDTO userDTO = new UserDTO();

        UserInfoDetailsDTO infoDetails =  new UserInfoDetailsDTO();
        infoDetails.setUsername(entity.getUsername());
        infoDetails.setFullName(entity.getFullName());
        infoDetails.setPhone(entity.getPhone());
        infoDetails.setEmail(entity.getEmail());
        infoDetails.setAvatarUrl(entity.getAvatarUrl());
        infoDetails.setEnabled(entity.isEnabled());
        infoDetails.setBlocked(entity.isBlocked());
        infoDetails.setCreatedAt(entity.getCreatedAt());

        userDTO.setId(entity.getId());
        userDTO.setInfoDetails(infoDetails);

        return userDTO;
    }

    public static String convertToRoleName(RoleForBatchDTO role) {
        return role.getName().toString();
    }

}
