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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//    @Autowired
//    private ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

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
                            requestDto.getPassword(),
                            null
                    )
            );

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.INVALID_LOGIN_REQUEST);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        Long userId = userDetails.getUser().getId();
        String email = userDetails.getUser().getEmail();
        Role role = userDetails.getUser().getRole();
        String token = jwtUtil.createToken(userId, email, role);
        // 응답 타입 JSON 설정 및 인코딩 지정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 응답 Dto 생성
        LoginResponseDto responseDto = new LoginResponseDto(token);
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
