package booking_platform.mapper;

import booking_platform.dto.UserDTO;
import booking_platform.projection.UserProjection;


public class UserMapper {


    public static UserDTO userDTO(UserProjection projection) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(projection.getId());

        return userDTO;
    }
}
