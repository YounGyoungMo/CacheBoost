package com.example.CacheBoost.common.security.config;


import com.example.CacheBoost.common.security.UserDetailsServiceImpl;
import com.example.CacheBoost.common.security.handler.CustomAccessDeniedHandler;
import com.example.CacheBoost.common.security.handler.CustomAuthenticationEntryPoint;
import com.example.CacheBoost.common.security.jwt.JwtAuthenticationFilter;
import com.example.CacheBoost.common.security.jwt.JwtAuthorizationFilter;
import com.example.CacheBoost.common.security.jwt.JwtUtil;
import com.example.CacheBoost.domain.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    // 레디스 저장소
    private final RedisTemplate<String, String> redisTemplate;

    // 예외처리
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    // 인증 예외 화이트리스트
    private static final String[] USER_WHITE_LIST = {
            "/api/auth/**"
    };

    private static final String[] ADMIN_WHITE_LIST = {
            "/admin/**",
    };
    // 비밀번호 암호화
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JwtAuthenticationFilter에 주입하기 위한 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 인증 필터
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
        // RedisTemplate 주입
        jwtAuthenticationFilter.setRedisTemplate(redisTemplate);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return jwtAuthenticationFilter;
    }

    // 인가 필터
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AccessDeniedHandler accessDeniedHandler) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        // ✅ 로그인은 필터를 거쳐야 하므로 permitAll 제거
//                        .requestMatchers(LOGIN_URL).authenticated()
                        .requestMatchers(USER_WHITE_LIST).permitAll()// 화이트리스트 요청 접근 허가
                        .requestMatchers(ADMIN_WHITE_LIST).hasAuthority(Role.ADMIN.getKey())
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리

        );
        // 예외 처리 추가
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
