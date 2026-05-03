package com.catholic.ac.kr.booking_platform.auth.core.listerner;

import com.catholic.ac.kr.booking_platform.auth.core.event.ForgotPasswordEvent;
import com.catholic.ac.kr.booking_platform.auth.core.event.RegistrySuccessEvent;
import com.catholic.ac.kr.booking_platform.notification.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthListener {
    private final String BASE_URL;
    private final EmailService emailService;

    public AuthListener(@Value("${react.port}") String baseUrl, EmailService emailService) {
        this.BASE_URL = baseUrl;
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleRegistrySuccess(RegistrySuccessEvent event) {
        log.info("{}이(가) 정상적으로 가입하였다. Email: {} - Number phone: {}",
                event.getUsername(), event.getEmail(), event.getPhone());

        String verifyLink = BASE_URL + "/verify?token=" + event.getTokenVerify();

        String body = """
                <div style="max-width: 600px; margin: 0 auto; font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; color: #333; line-height: 1.6; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
                    <div style="background-color: #2d89ef; padding: 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 24px;">CUK Booking 계정 인증</h1>
                    </div>
                    <div style="padding: 30px; background-color: #ffffff;">
                        <h2 style="margin-top: 0;">안녕하세요, %s님!</h2>
                        <p>CUK Booking(Catholic Social)에 가입해 주셔서 진심으로 감사드립니다.</p>
                        <p>서비스 이용을 시작하기 위해 아래의 버튼을 클릭하여 <strong>이메일 주소를 인증</strong>해 주세요.</p>
                
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="%s"
                               style="background-color: #2d89ef; color: #ffffff; padding: 15px 35px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block; font-size: 16px;">
                               이메일 인증하기
                            </a>
                        </div>
                
                        <p style="font-size: 14px; color: #666;">버튼이 클릭되지 않는 경우, 아래 링크를 복사하여 브라우저에 붙여넣어 주세요:</p>
                        <p style="font-size: 12px; word-break: break-all;"><a href="%s" style="color: #2d89ef;">%s</a></p>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eee;">
                        <p>본 이메일은 발신 전용입니다. 계정 생성을 요청하지 않으셨다면 이 이메일을 무시해 주세요.</p>
                        <p>&copy; 2026 Catholic University of Korea. All rights reserved.</p>
                    </div>
                </div>
                """.formatted(event.getUsername(), verifyLink, verifyLink, verifyLink);

        emailService.sendEmail(
                event.getEmail(),
                "[CUK Booking] 회원 가입 계정 인증 안내",
                body);
    }

    @Async
    @EventListener
    public void handleForgotPassword(ForgotPasswordEvent event) {

        String verifyLink = BASE_URL + "/forgot-password?token=" + event.token();

        String body = """
                <div style="max-width: 600px; margin: 0 auto; font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; color: #333; line-height: 1.6; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
                
                    <div style="background-color: #e81123; padding: 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 24px;">비밀번호 재설정</h1>
                    </div>
                
                    <div style="padding: 30px; background-color: #ffffff;">
                        <h2 style="margin-top: 0;">안녕하세요, %s님!</h2>
                
                        <p>비밀번호 재설정 요청이 접수되었습니다.</p>
                        <p>아래 버튼을 클릭하여 <strong>새 비밀번호를 설정</strong>해 주세요.</p>
                
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="%s"
                               style="background-color: #e81123; color: #ffffff; padding: 15px 35px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block; font-size: 16px;">
                               비밀번호 재설정
                            </a>
                        </div>
                
                        <p style="font-size: 14px; color: #666;">
                            이 링크는 <strong>15분 동안만 유효</strong>합니다. 보안을 위해 가능한 빨리 진행해 주세요.
                        </p>
                
                        <p style="font-size: 14px; color: #666;">
                            버튼이 클릭되지 않는 경우 아래 링크를 복사하여 사용하세요:
                        </p>
                
                        <p style="font-size: 12px; word-break: break-all;">
                            <a href="%s" style="color: #e81123;">%s</a>
                        </p>
                    </div>
                
                    <div style="background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eee;">
                        <p>비밀번호 재설정을 요청하지 않으셨다면 이 이메일을 무시해 주세요.</p>
                        <p>&copy; 2026 Catholic University of Korea. All rights reserved.</p>
                    </div>
                
                </div>
                """.formatted(
                event.userFullName(),
                verifyLink,
                verifyLink,
                verifyLink
        );

        emailService.sendEmail(
                event.email(),
                "[CUK Booking] 비밀번호 재설정 안내",
                body
        );
    }
}
