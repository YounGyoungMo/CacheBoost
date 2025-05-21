package com.example.CacheBoost.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 로그 출력
        log.warn("[401] 인증 실패 - 요청 URI: {}, 메서드: {}, 에러: {}",
                request.getRequestURI(),
                request.getMethod(),
                authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        String body = """
                    {
                      "status": 401,
                      "code": "UNAUTHORIZED",
                      "message": "인증이 필요합니다. 로그인 후 다시 시도해주세요."
                    }
                """;

        response.getWriter().write(body);
    }
}
