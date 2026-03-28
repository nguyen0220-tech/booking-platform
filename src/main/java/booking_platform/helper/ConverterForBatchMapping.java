package booking_platform.helper;

import booking_platform.dto.RoleForBatchDTO;
import booking_platform.dto.UserInfoDetailsDTO;
import booking_platform.model.User;

public class ConverterForBatchMapping {
    public static UserInfoDetailsDTO convertToUserInFfo(User user) {
        UserInfoDetailsDTO userInfoDetailsDTO = new UserInfoDetailsDTO();

        userInfoDetailsDTO.setUsername(user.getUsername());
        userInfoDetailsDTO.setFullName(user.getFullName());
        userInfoDetailsDTO.setPhone(user.getPhone());
        userInfoDetailsDTO.setEmail(user.getEmail());
        userInfoDetailsDTO.setAvatarUrl(user.getAvatarUrl());
        userInfoDetailsDTO.setEnabled(user.isEnabled());
        userInfoDetailsDTO.setCreatedAt(user.getCreatedAt());

        return userInfoDetailsDTO;
    }

    public static String convertToRoleName(RoleForBatchDTO role) {
        return role.getName().toString();
    }

}
