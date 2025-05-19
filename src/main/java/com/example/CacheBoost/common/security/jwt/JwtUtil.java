package com.example.CacheBoost.common.security.jwt;


import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // 토큰 유효시간 60분
    private static final long TOKEN_TIME = 1000 * 60 * 60L;
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";     // 관례

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

    /**
     * Access Token 발급
     */
    public String createToken(Long userId, String email, Role role) {

        // payLoad
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))         // 토큰 주체
                .setHeaderParam("typ", "JWT")   // 헤더 설정, 생략 가능(디버깅용)
                .setIssuedAt(now)       // 발행 시간
                .setExpiration(new Date(now.getTime() + TOKEN_TIME))   // 토큰 만료기한 (발급 일시 +60분)
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
     * 토큰에서 바디(Claims) 추출
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
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

}
