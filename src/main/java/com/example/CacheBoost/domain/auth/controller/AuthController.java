package com.example.CacheBoost.domain.auth.controller;


import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.common.security.dto.LoginRequestDto;
import com.example.CacheBoost.common.security.dto.LoginResponseDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @PostMapping("/login")
    public void login(
            @RequestBody @Valid LoginRequestDto requestDto
    ) {
        log.info("컨트롤러 로그인 요청");
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(ApiResponseDto.success(SuccessCode.LOGIN_SUCCESS, responseDto));
    }
}
