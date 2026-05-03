package com.catholic.ac.kr.booking_platform.infrastructure.components;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class Logging {
    @Around("execution(* com.catholic.ac.kr.booking_platform.notification.*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("==> {}.{}() 호출",className,methodName);


        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        long runtime = endTime - startTime;

        log.info("<== {}.{}() 실행 끝 in {}ms",className,methodName,runtime);

        return result;
    }
}
