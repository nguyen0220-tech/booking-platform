package com.catholic.ac.kr.booking_platform.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver handlerExceptionResolver;

    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException) {
        handlerExceptionResolver.resolveException(request, response, null, authException);
    }
}

/*
Nếu người dùng gửi request mà Session đã hết hạn hoặc chưa đăng nhập,
Spring Security sẽ chặn request ngay tại tầng Filter và ném ra AuthenticationException
(ví dụ: InsufficientAuthenticationException).

Hậu quả: Request bị dội ngược lại ngay tại Filter.
Lỗi này KHÔNG bao giờ đi tới được GlobalExceptionHandler

Mặc định, Spring Security với Session sẽ trả về lỗi 403 Forbidden (nếu là API)
hoặc trả về mã HTML (White-label Error Page) hoặc redirect 302 về trang /login.
Nó sẽ không trả về JSON ApiResponse như đã cấu hình!

Giải pháp: Cầu nối giữa Filter và GlobalExceptionHandler
Để Frontend luôn nhận được một format chuẩn (là class ApiResponse ) kể cả khi Session hết hạn,
cần tạo một AuthenticationEntryPoint để "hứng" lỗi từ Filter, sau đó "ném" nó vào cho @RestControllerAdvice xử lý.
Khi người dùng bị mất Session (do để máy lâu quá timeout, hoặc gọi API mà quên gửi cookie JSESSIONID),

Luồng sẽ chạy như sau:
Spring Security Filter phát hiện không có Session hợp lệ -> Ném AuthenticationException.
CustomAuthenticationEntryPoint bắt được lỗi.
Chuyển thẳng lỗi này cho hàm @ExceptionHandler(AuthenticationException.class) trong file GlobalExceptionHandler.
Frontend nhận được mã JSON 401 chuẩn:
 */
