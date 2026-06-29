package com.catholic.ac.kr.booking_platform.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
   관리자가 사용자를 관리하기 위한 dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserDTO {
    private Long id;

    private UserInfoDetailsDTO infoDetails;
}
