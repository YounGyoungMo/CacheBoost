package com.example.CacheBoost.common.security.jwt;


import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.auth.dto.TokenPayload;
import com.example.CacheBoost.domain.user.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // 액세스 토큰 유효시간 30분
    private static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60L * 24 * 7;

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";     // 관례
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    // 비밀키 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String extendAccessToken(TokenPayload tokenPayload,long expireTimeMs) {
        // AccessToken 만료 시간 30분
        return createToken(tokenPayload.getUserId(), tokenPayload.getEmail(), tokenPayload.getRole(), expireTimeMs);
    }

    public String createAccessToken(TokenPayload tokenPayload) {
        // AccessToken 만료 시간 30분
        return createToken(tokenPayload.getUserId(), tokenPayload.getEmail(), tokenPayload.getRole(), ACCESS_TOKEN_TIME);
    }

    public String createRefreshToken(Long userId) {
        // RefreshToken 만료 시간 7일
        return createToken(userId, null, null, REFRESH_TOKEN_TIME);
    }
    

    /**
     *  Token 발급
     */
    public String createToken(Long userId, String email, Role role,long expireTimeMs) {

        // payLoad
        Date now = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))         // 토큰 주체
                        .setHeaderParam("typ", "JWT")   // 헤더 설정, 생략 가능(디버깅용)
                        .setIssuedAt(now)       // 발급일(발행 시간)
                        .setExpiration(new Date(now.getTime() + expireTimeMs))   // 토큰 만료기한 (발급 일시 +30분)
                        .claim("userId", userId)    // Private Claims (Key-Value)
                        .claim("email", email)
                        .claim("role", role)
                        .signWith(key, signatureAlgorithm)  // 서명 (사용 알고리즘, 서명 생성-검증 용 비밀 키)
                        .compact();

    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ErrorCode.MISSING_TOKEN);
    }

    /**
     * 토큰에서 바디(Claims) 추출(토큰에서 사용자 정보 추출)
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 실제 JWT 파싱 + 서명 검증
             // 파싱 성공하면 토큰 유효
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(ErrorCode.INVALID_JWT_SIGNATURE);
        }catch (ExpiredJwtException e) {
            logger.error("만료된 JWT token 입니다. token = {}", token, e);
            throw new CustomException(ErrorCode.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            logger.error("잘못된 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.EMPTY_JWT);
        }
    }

    //  만료일 확인 메소드
    public boolean isExpired(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // userId 가져오기
    public Long getUserIdFromToken(String token) {
        return extractClaims(token).get("userId", Long.class);
    }
    // email 가져오기
    public String getUserEmailFromToken(String token) {
        return extractClaims(token).get("email", String.class);
    }
    // Role 가져오기
    public Role getUserRoleFromToken(String token) {
        return extractClaims(token).get("role", Role.class);
    }


    // 남은 기간 반환 메서드
    public long getExpiration(String token) {
        Date expiration = extractClaims(token).getExpiration();
        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }

    // 토큰을 통해 페이로드 추출
    public TokenPayload getPayload(String token) {
        Long userId = getUserIdFromToken(token);
        String email = getUserEmailFromToken(token);
        Role role = getUserRoleFromToken(token);
        return TokenPayload.builder()
                .userId(userId)
                .email(email)
                .role(role)
                .build();
    }
}
