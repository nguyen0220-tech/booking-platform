package com.catholic.ac.kr.booking_platform.infrastructure.components;

import com.catholic.ac.kr.booking_platform.infrastructure.exception.TooManyRequestsException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES) //5분 동안 요청 없는 ip는 메모리에서 없앰
            .maximumSize(10000) //메모리 제한: 10001번째 요청 있을 때 가장 적게 사용하는 ip 제거 (Window TinyLFU 알고리즘)
            .build();

    private Bucket createBucket() {
        Refill refill = Refill.greedy(5, Duration.ofMinutes(1));

        Bandwidth limit = Bandwidth.classic(5, refill);

        return Bucket.builder().addLimit(limit).build();
    }

    /*
    Khi ứng dụng của bạn chạy thực tế, nó thường đứng sau một Proxy hoặc Load Balancer (ví dụ: Nginx, AWS ALB).
    Nếu gọi request.getRemoteAddr(),sẽ chỉ lấy được IP của cái Nginx chứ không phải IP thật của khách.
    Nginx thường sẽ nhét IP thật của khách vào một Header có tên là x-forwarded-for.
    Header này có thể chứa một chuỗi nhiều IP cách nhau bằng dấu phẩy nếu đi qua nhiều proxy
    (VD: IP_Khách, IP_Proxy1, IP_Proxy2). Hàm split(",")[0] giúp bạn lấy chính xác cái IP đầu tiên — tức là IP thật của người dùng.
    => X-Forwarded-For: <IP_user>, <IP_Proxy_1>, <IP_Proxy_2>
     */
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("x-forwarded-for");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    public boolean preHandle(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final Object handler) throws IOException {

        String ipAddress = getClientIp(request);
        log.debug("IP Address: {}", ipAddress);

        Bucket bucket = buckets.get(ipAddress, k -> createBucket());

        if (bucket != null && bucket.tryConsume(1)) {
            return true;
        } else {
            throw new TooManyRequestsException("과도한 요청입니다.잠시 후 다시 시도하세요");
        }
    }

    /* Ví dụ về trường hợp cụ thế khi một người dùng thực hiện hành đông
    Người dùng đi qua 2 Proxy (Ví dụ: Client -> Cloudflare -> Nginx -> Server)
    Chuỗi nhận được từ Header (xfHeader): "171.224.200.15, 104.18.23.11, 192.168.1.5"
    (Trong đó: 171.224.200.15 là IP nhà mạng của khách, các IP sau là của Cloudflare và Nginx)

    Bước 1: Chạy .split(",")
    Nó sẽ trả về một mảng String gồm 3 phần tử:
    ["171.224.200.15", " 104.18.23.11", " 192.168.1.5"]

    Bước 2: Chạy [0]
    Nó trỏ vào phần tử đầu tiên của mảng:
    Kết quả cuối cùng: "171.224.200.15" (Đúng IP thật của người dùng).
     */

}
