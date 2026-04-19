package com.catholic.ac.kr.booking_platform.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrySuccessEvent {
    private String username;
    private String email;
    private String phone;
    private String tokenVerify;
}
