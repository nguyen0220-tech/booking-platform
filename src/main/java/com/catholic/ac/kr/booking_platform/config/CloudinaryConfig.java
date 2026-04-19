package com.catholic.ac.kr.booking_platform.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME;
    private final String API_KEY;
    private final String API_SECRET;

    public CloudinaryConfig(
            @Value("${cd.cloud_name}") String cloudName,
            @Value("${cd.api_key}") String apiKey,
            @Value("${cd.api_secret}") String apiSecret) {
        this.CLOUD_NAME = cloudName;
        this.API_KEY = apiKey;
        this.API_SECRET = apiSecret;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", CLOUD_NAME,
                        "api_key", API_KEY,
                        "api_secret", API_SECRET,
                        "secure", true
                )
        );
    }
}