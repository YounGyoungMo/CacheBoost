package com.example.CacheBoost.domain.auth.controller;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.common.security.dto.LoginResponseDto;
import com.example.CacheBoost.common.security.jwt.JwtUtil;
import com.example.CacheBoost.domain.auth.AuthUser;
import com.example.CacheBoost.domain.auth.dto.AccessTokenResponseDto;
import com.example.CacheBoost.domain.auth.dto.TokenPayload;
import com.example.CacheBoost.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "🔐 AUTH")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    // 액세스 토큰 재발급시 user정보 찾아야함
    private final UserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> refreshToken(
            @RequestHeader("Authorization") String refreshTokenHeader
    ) {
        // prefix 제거
        String refreshToken = jwtUtil.substringToken(refreshTokenHeader);
        // 1. 리프래시 토큰 검증
        jwtUtil.validateToken(refreshToken);

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String redisKey = "RT:" + userId;
        String storedRT = redisTemplate.opsForValue().get(redisKey);
        // 2. 레디스에 저장된 리프래시 토큰 없는 경우
        if (storedRT == null ) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        } else if (!storedRT.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        // 3. userId로 토큰 생성시 필요한 dto 가져오기
        TokenPayload tokenPayload = userService.getTokenPayloadByUserId(userId);
        // 4. 액세스 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken(tokenPayload);
        log.info("액세스 토큰 재발급 성공");
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.TOKEN_REFRESH_SUCCESS,
                new LoginResponseDto(newAccessToken, refreshToken)));
    }

    // 리프래시 토큰없이 액세스 토큰만 유효시간 늘리고 싶을때
    // (리프래시 토큰은 만료되고 현재 로그인된 액세스 토큰만 남은 상황 가정)
    @PostMapping("/extend-access-token")
    public ResponseEntity<ApiResponseDto<AccessTokenResponseDto>> extendAccessToken(
            @RequestHeader("Authorization") String accessTokenHeader,
            @AuthUser Long userId
    ) {
        String accessToken = jwtUtil.substringToken(accessTokenHeader);

        // 1. 토큰 검증
        jwtUtil.validateToken(accessToken);

        // 2. 남은 시간 검사 (선택)
        long remainingMs = jwtUtil.getExpiration(accessToken);
        // 남은 시간 5분 이하일때만 연장 가능
        if (remainingMs > Duration.ofMinutes(5).toMillis()) {
            throw new CustomException(ErrorCode.TOO_EARLY_TO_EXTEND);
        }
        // 3. 토큰 정보 추출
        TokenPayload payload = jwtUtil.getPayload(accessToken);

        String redisKey = "Extended:"+userId;
        
        // 4. 토큰 연장 횟수 체크
        String countStr = redisTemplate.opsForValue().get(redisKey);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        if (count >= 3) {
            throw new CustomException(ErrorCode.EXTEND_LIMIT_EXCEEDED);
        }

        // 5. 연장 + Redis 카운트 증가
        redisTemplate.opsForValue().set(redisKey, String.valueOf(count + 1), Duration.ofMinutes(30));

        // 6. 액세스 토큰(재발급)
        remainingMs = jwtUtil.getExpiration(accessToken);

        String extendedAccessToken = jwtUtil.extendAccessToken(payload, remainingMs + Duration.ofMinutes(15).toMillis());
        AccessTokenResponseDto responseDto = new AccessTokenResponseDto(extendedAccessToken);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.ACCESS_TOKEN_EXTENDED, responseDto));


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
