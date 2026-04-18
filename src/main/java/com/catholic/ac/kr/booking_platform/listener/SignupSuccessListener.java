package com.catholic.ac.kr.booking_platform.listener;

import com.catholic.ac.kr.booking_platform.event.RegistrySuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SignupSuccessListener {

    @EventListener
    public void handleSignupSuccess(RegistrySuccessEvent event) {
        log.info("{}이(가) 정상적으로 가입하였다. Email: {} - Number phone: {}", event.getUsername(), event.getEmail(), event.getPhone());
    }
}
