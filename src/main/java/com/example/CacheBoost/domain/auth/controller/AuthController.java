package com.example.CacheBoost.domain.auth.controller;

import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.common.security.dto.LoginResponseDto;
import com.example.CacheBoost.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> refreshToken(
            @RequestHeader("Authorization") String refreshTokenHeader
    ) {
        // prefix 제거
        String refreshToken = jwtUtil.substringToken(refreshTokenHeader);
        // 리프래시 토큰 검증
        jwtUtil.validateToken(refreshToken);

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtUtil.createRefreshToken(userId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.TOKEN_REFRESH_SUCCESS,
                new LoginResponseDto(newAccessToken, refreshToken)));
    }
}
