package com.catholic.ac.kr.booking_platform;

import com.catholic.ac.kr.booking_platform.booking.constant.PayMethod;
import com.catholic.ac.kr.booking_platform.booking.core.BookingService;
import com.catholic.ac.kr.booking_platform.booking.data.PackageAvailabilityRepository;
import com.catholic.ac.kr.booking_platform.booking.dto.BookingRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@ActiveProfiles("test")
class BookingOptimisticLockTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PackageAvailabilityRepository availabilityRepository;

    @Test
    void testOptimisticLocking_ShouldThrowException() throws InterruptedException {
        Long userIdA = 1L; // Đảm bảo ID này có trong DB test
        Long userIdB = 2L;
        Long packageId = 10L; // Gói test
        LocalDate usageDate = LocalDate.of(2026, 6, 5);

        // Khởi tạo sẵn data ban đầu với version = 0
        // (Hoặc để hệ thống tự tạo ở request đầu tiên)

        // Tạo một Pool gồm 2 luồng chạy song song
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // CountDownLatch giúp giữ 2 luồng lại, đợi lệnh là cùng xuất phát một lúc
        CountDownLatch latch = new CountDownLatch(1);

        // Biến hứng kết quả lỗi
        AtomicReference<Exception> exceptionThrown = new AtomicReference<>();

        BookingRequest requestA = new BookingRequest(packageId, usageDate, LocalTime.of(18, 0), PayMethod.KAKAO_PAY);
        BookingRequest requestB = new BookingRequest(packageId, usageDate, LocalTime.of(18, 0), PayMethod.KAKAO_PAY);

        // Luồng của Sinh viên A
        executor.submit(() -> {
            try {
                latch.await(); // Chờ súng nổ
                bookingService.createBooking(userIdA, requestA);
            } catch (Exception e) {
                exceptionThrown.set(e);
            }
        });

        // Luồng của Sinh viên B
        executor.submit(() -> {
            try {
                latch.await(); // Chờ súng nổ
                bookingService.createBooking(userIdB, requestB);
            } catch (Exception e) {
                exceptionThrown.set(e);
            }
        });

        // ĐẾM NGƯỢC... BẮT ĐẦU! Cho 2 luồng lao vào cùng lúc
        latch.countDown();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // ĐOẠN KIỂM TRA (ASSERTION)
        // Chắc chắn phải có 1 trong 2 đứa thất bại và ném ra lỗi do tranh chấp version
        Assertions.assertNotNull(exceptionThrown.get());
        System.out.println("Lỗi ném ra thực tế: " + exceptionThrown.get().getClass().getName());

        // Nếu qua tầng Service lỗi đã bị bọc lại, bạn có thể check thuộc tính nguyên nhân ngầm định (Cause)
        // Assertions.assertTrue(exceptionThrown.get().getCause() instanceof ObjectOptimisticLockingFailureException);
    }
}