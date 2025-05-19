package com.example.CacheBoost.common.advice;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 커스텀 예외(CustomException)를 처리하는 핸들러
     * - 개발자가 정의한 도메인/비즈니스 예외
     * - 예: 검색 실패 등
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto<?>> handleCustomException(CustomException e, HttpServletRequest httpServletRequest) {

        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponseDto.fail(e.getErrorCode(), httpServletRequest.getRequestURI()));

    }


}
