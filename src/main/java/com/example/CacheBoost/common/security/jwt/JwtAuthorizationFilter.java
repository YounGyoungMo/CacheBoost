package com.example.CacheBoost.common.security.jwt;

import com.example.CacheBoost.common.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * OncePerRequestFilter를 상속받아서 Http 요청마다 단 한 번 실행되는 필터
 * 토큰 검증 기능
 * Authentication(인증 객체)를 SecurityContext 홀더의 SecurityContext에 등록함
 */
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    // 이메일을 통해 사용자 정보 가져오기 위한 서비스
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰
        String tokenValue = request.getHeader("Authorization");

        if (StringUtils.hasText(tokenValue)) {
            // "Bearer " 제거 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            // 토큰 검증 로직(유효성 검사)
            jwtUtil.validateToken(tokenValue);
            // 토큰에서 사용자 정보 추출
            Claims claims = jwtUtil.extractClaims(tokenValue);

            try {
                setAuthenticaiton((String) claims.get("email"));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthenticaiton(String email) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = createAuthentication(email);
        securityContext.setAuthentication(authentication);

    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // UsernamePasswordAuthenticationToken: 스프링 시큐리티에서 가장 기본적인 Authentication 구현체
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
