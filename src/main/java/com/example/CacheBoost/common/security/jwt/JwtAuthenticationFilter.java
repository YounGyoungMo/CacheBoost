package com.example.CacheBoost.common.security.jwt;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.common.response.ErrorData;
import com.example.CacheBoost.common.security.UserDetailsImpl;
import com.example.CacheBoost.common.security.dto.LoginRequestDto;
import com.example.CacheBoost.common.security.dto.LoginResponseDto;
import com.example.CacheBoost.domain.user.entity.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private RedisTemplate<String, String> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    /**
     * AuthenticationManager: 로그인 인증을 위임할 객체(UsernamePasswordAuthenticationFilter에서 가져옴)
     * JwtUtil: 로그인 성공 시 JWT를 발급하는 유틸 클래스
     * @return
     * @throws // ErrorCode.INVALID_LOGIN_REQUEST
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            // 로그인 요청의 JSON 바디를 읽어서 Dto에 바인딩
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            // authenticationManager에게 인증 위임
            return getAuthenticationManager().authenticate(
                    // UsernamePasswordAuthenticationToken( 유저 인증 정보 생성)
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.INVALID_LOGIN_REQUEST);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        Long userId = userDetails.getUser().getId();
        String email = userDetails.getUser().getEmail();
        Role role = userDetails.getUser().getRole();
        // 액세스 토큰과 리프래쉬 토큰 발급
        String accessToken = jwtUtil.createAccessToken(userId, email, role);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        try {
            // 레디스에 리프래시 토큰 7일 저장
            redisTemplate.opsForValue().set("RT:" + userId, refreshToken, 7, TimeUnit.DAYS);
        } catch (RedisConnectionFailureException e) {
            log.error("❌ Redis 연결 실패 - RefreshToken 저장 불가 : {}", e.getMessage());
            throw new CustomException(ErrorCode.REDIS_CONNECTION_FAILURE);
        }
        log.info("로그인 성공 및 JWT 생성");
        // 응답 타입 JSON 설정 및 인코딩 지정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 응답 Dto 생성
        LoginResponseDto responseDto = new LoginResponseDto(accessToken,refreshToken);
        ApiResponseDto<LoginResponseDto> successResponse = ApiResponseDto.success(SuccessCode.LOGIN_SUCCESS, responseDto);

        response.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        // 상태코드 401
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 요청 URI
        String requestPath = request.getRequestURI();
        ApiResponseDto<ErrorData> failResponse = ApiResponseDto.fail(ErrorCode.LOGIN_FAILED, requestPath);

        response.getWriter().write(new ObjectMapper().writeValueAsString(failResponse));
    }
}
