package com.catholic.ac.kr.booking_platform.facility.core.listener;

import com.catholic.ac.kr.booking_platform.facility.core.event.FacilityApprovalEvent;
import com.catholic.ac.kr.booking_platform.facility.core.event.FacilityRejectionEvent;
import com.catholic.ac.kr.booking_platform.notification.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FacilityRegistrationListener {
    private final String BASE_URL;
    private final EmailService emailService;

    public FacilityRegistrationListener(@Value("${react.port}") String baseUrl, EmailService emailService) {
        this.BASE_URL = baseUrl;
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleFacilityRegistrationApprovalEvent(FacilityApprovalEvent event) {
        log.info("Facility Approved: {} - Owner: {}", event.facilityName(), event.ownerEmail());

        String redirectLink = BASE_URL + "/facilities";

        String body = """
                <div style="max-width: 600px; margin: 0 auto; font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; color: #333; line-height: 1.6; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
                    <div style="background-color: #107c41; padding: 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 24px;">시설 등록 승인 안내</h1>
                    </div>
                    <div style="padding: 30px; background-color: #ffffff;">
                        <h2 style="margin-top: 0;">안녕하세요, %s님!</h2>
                        <p>CUK Booking을 이용해 주셔서 감사합니다.</p>
                        <p>신청하신 시설 <strong>[%s]</strong>의 등록이 성공적으로 <strong>승인</strong>되었습니다.</p>
                        <p>이제부터 해당 시설 của bạn đã sẵn sàng để đón nhận lượt đặt chỗ từ người dùng. 아래 버튼을 클릭하여 대시보드 từ quản lý ngay.</p>
                
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="%s"
                               style="background-color: #107c41; color: #ffffff; padding: 15px 35px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block; font-size: 16px;">
                               시설 관리하러 가기
                            </a>
                        </div>
                
                        <p style="font-size: 14px; color: #666;">버튼이 클릭되지 않는 경우, 아래 링크를 복as하여 브라우저에 붙여넣어 주세요:</p>
                        <p style="font-size: 12px; word-break: break-all;"><a href="%s" style="color: #107c41;">%s</a></p>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eee;">
                        <p>본 이메일은 발신 전용입니다. 문의 사항이 있으시면 고객센터로 연락해 주세요.</p>
                        <p>&copy; 2026 Catholic University of Korea. All rights reserved.</p>
                    </div>
                </div>
                """.formatted(event.ownerName(), event.facilityName(), redirectLink, redirectLink, redirectLink);

        emailService.sendEmail(
                event.ownerEmail(),
                "[CUK Booking] 신청하신 시설 등록이 승인되었습니다.",
                body);
    }

    @Async
    @EventListener
    public void handleFacilityRegistrationRejectEvent(FacilityRejectionEvent event) {
        log.warn("Facility Rejected: {} - Owner: {}", event.facilityName(), event.ownerEmail());

        String redirectLink = BASE_URL + "/facilities";

        String body = """
                <div style="max-width: 600px; margin: 0 auto; font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; color: #333; line-height: 1.6; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
                    <div style="background-color: #d9534f; padding: 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 24px;">시설 등록 반려 안내</h1>
                    </div>
                    <div style="padding: 30px; background-color: #ffffff;">
                        <h2 style="margin-top: 0;">안녕하세요, %s님!</h2>
                        <p>CUK Booking을 이용해 주셔서 감사합니다.</p>
                        <p>안타깝게도 신청하신 시설 <strong>[%s]</strong>의 등록이 <strong>반려</strong>되었음을 안내드립니다.</p>
                        <p>반려 사유 등 자세한 사항은 아래 버튼을 통해 대시보드에 접속하신 후, 신청 내역에서 확인해 주시기 바랍니다. 수정 후 재신청이 가능합니다.</p>
                
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="%s"
                               style="background-color: #d9534f; color: #ffffff; padding: 15px 35px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block; font-size: 16px;">
                               등록 내역 확인하기
                            </a>
                        </div>
                
                        <p style="font-size: 14px; color: #666;">버튼이 클릭되지 않는 경우, 아래 링크를 복사하여 브라우저에 붙여넣어 주세요:</p>
                        <p style="font-size: 12px; word-break: break-all;"><a href="%s" style="color: #d9534f;">%s</a></p>
                    </div>
                    <div style="background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eee;">
                        <p>본 이메일은 발신 전용입니다. 관련하여 의문 사항이 있으시면 hỗ trợ kỹ thuật qua trang web của trường.</p>
                        <p>&copy; 2026 Catholic University of Korea. All rights reserved.</p>
                    </div>
                </div>
                """.formatted(event.ownerName(), event.facilityName(), redirectLink, redirectLink, redirectLink);

        emailService.sendEmail(
                event.ownerEmail(),
                "[CUK Booking] 신청하신 시설 등록이 반려되었습니다.",
                body);
    }
}