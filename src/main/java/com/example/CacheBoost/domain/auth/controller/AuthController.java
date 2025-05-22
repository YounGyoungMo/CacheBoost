package com.example.CacheBoost.domain.auth.controller;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.common.security.dto.LoginResponseDto;
import com.example.CacheBoost.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "🔐 AUTH")
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
        String redisKey = "RT:" + userId;
        String storedRT = redisTemplate.opsForValue().get(redisKey);
        // 레디스에 저장된 리프래시 토큰 없는 경우
        if (storedRT == null ) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        } else if (!storedRT.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        log.info("액세스 토큰 재발급 성공");
        String newAccessToken = jwtUtil.createRefreshToken(userId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.TOKEN_REFRESH_SUCCESS,
                new LoginResponseDto(newAccessToken, refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout(
            @RequestHeader("Authorization") String accessTokenHeader
    ) {
        String accessToken = jwtUtil.substringToken(accessTokenHeader);

        // 토큰 검증
        jwtUtil.validateToken(accessToken);

        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        // 1. key값으로 리프래쉬 토큰 지우기
        redisTemplate.delete("RT:" + userId);

        // 2. 액세스 토큰 블랙리스트 처리
        long remainingMs = jwtUtil.getExpiration(accessToken);
        redisTemplate.opsForValue().set("BL:" + accessToken, "logout", remainingMs, TimeUnit.MILLISECONDS);
        log.info("로그아웃 완료: 리프래시 토큰 삭제 및 액세스 토큰 블랙리스트 등록");
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.LOGOUT_SUCCESS));
    }
}
