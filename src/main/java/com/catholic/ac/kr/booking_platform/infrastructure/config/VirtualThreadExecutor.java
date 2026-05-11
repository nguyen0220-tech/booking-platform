package com.catholic.ac.kr.booking_platform.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class VirtualThreadExecutor {
    @Bean
    public ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
