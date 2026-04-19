package com.catholic.ac.kr.booking_platform.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    private final Resend resend;

    public EmailService(@Value("${resend.api.key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendEmail(String to, String subject, String body) {

        /*
            Resend는 확정된(verify) domain 필요함으로 인해 가상 이메일 보내기 상황임.
            실제는 to: 사용자의 아메일임
         */

        CreateEmailOptions request = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")  // dev 위해 resend 제공하는 이메일
                .to("teemee202@gmail.com")
                .subject(subject)
                .html(body)
                .build();

        try {
            resend.emails().send(request);
            log.info("Send email successful to: {} ", to);
        } catch (Exception e) {
            log.error("Send email failed: {}", e.getMessage());
        }
    }
}
