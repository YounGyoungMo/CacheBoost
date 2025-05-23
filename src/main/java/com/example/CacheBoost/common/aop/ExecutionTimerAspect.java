package com.example.CacheBoost.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ExecutionTimerAspect {
    private final HttpServletRequest request;

    @Around("@annotation(timer)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, ExecutionTimer timer) throws Throwable {
//        long start = System.currentTimeMillis(); // 밀리초
        long start = System.nanoTime(); // 나노초
        // 비즈니스 로직 실행
        Object result = joinPoint.proceed();

//        long end = System.currentTimeMillis(); // 밀리초
        long end = System.nanoTime(); // 나노초
        long duration = (end - start) / 1_000_000; // 나노초 → 밀리초 변환
        String methodName = joinPoint.getSignature().toShortString();
        String tag = timer.value().isEmpty() ? methodName : timer.value();

        String url = request.getRequestURI(); // 요청 URL
        String method = request.getMethod();  // GET, POST 등
        String ip = request.getRemoteAddr();  // 요청자 IP

        log.info("⏱ [ExecutionTimer] {} | Method: [{}], URl: [{}] | 실행 시간: {}ms",
                tag, method, url, duration);

        return result;
    }
}
