package com.catholic.ac.kr.booking_platform.listener;

import com.catholic.ac.kr.booking_platform.event.UpdateProfileEvent;
import com.catholic.ac.kr.booking_platform.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UpdateEmailListener {
    private final String BASE_URL;
    private final EmailService emailService;

    public UpdateEmailListener(@Value("${react.port}") String baseUrl, EmailService emailService) {
        this.BASE_URL = baseUrl;
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleConfirmUpdateEmail(UpdateProfileEvent event) {
        String username = (event.getFullName() != null) ? event.getFullName() : "회원";
        String verifyLink = BASE_URL + "/profile/verify?token=" + event.getToken();

        String body = """
                <div style="max-width: 600px; margin: 0 auto; font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; color: #333; line-height: 1.6; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
                    <div style="background-color: #2d89ef; padding: 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 24px;">CUK Booking 이메일 변경 인증</h1>
                    </div>
                    <div style="padding: 30px; background-color: #ffffff;">
                        <h2 style="margin-top: 0;">안녕하세요, %s님!</h2>
                        <p>CUK Booking 서비스를 이용해 주셔서 감사합니다.</p>
                        <p>계정의 <strong>이메일 주소 변경</strong>을 완료하기 위해 아래의 버튼을 클릭하여 인증해 주세요.</p>
                        <p style="color: #e74c3c; font-size: 14px;">* 본 인증 링크는 15분 동안만 유효합니다.</p>
                
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="%s"
                               style="background-color: #2d89ef; color: #ffffff; padding: 15px 35px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block; font-size: 16px;">
                               이메일 주소 변경 확정
                            </a>
                        </div>
                
                        <p style="font-size: 14px; color: #666;">버튼이 클릭되지 않는 경우, 아래 링크를 복사하여 브라우저에 붙여넣어 주세요:</p>
                        <p style="font-size: 12px; word-break: break-all;"><a href="%s" style="color: #2d89ef;">%s</a></p>
                
                        <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">
                        <p style="font-size: 13px; color: #888;">
                            만약 이메일 변경을 요청하지 않으셨다면, 본 이메일을 무시해 주세요.
                            타인에 의해 요청된 경우 즉시 비밀번호를 변경하고 고객센터로 문의하시기 바랍니다.
                        </p>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eee;">
                        <p>본 이메일은 발신 전용입니다.</p>
                        <p>&copy; 2026 Catholic University of Korea. All rights reserved.</p>
                    </div>
                </div>
                """.formatted(username, verifyLink, verifyLink, verifyLink);

        emailService.sendEmail(event.getEmail(), "[CUK Booking] 이메일 변경 인증 안내", body);
    }
}
