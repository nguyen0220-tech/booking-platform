package com.catholic.ac.kr.booking_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class BookingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingPlatformApplication.class, args);
    }

}
