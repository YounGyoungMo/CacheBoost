package com.example.CacheBoost.domain.auth.controller;


import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.auth.dto.LoginRequestDto;
import com.example.CacheBoost.domain.auth.dto.LoginResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(
            @RequestBody @Valid LoginRequestDto requestDto
    ) {

        LoginResponseDto responseDto = null;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(SuccessCode.LOGIN_SUCCESS, responseDto));
    }
}
