package com.example.CacheBoost.domain.auth;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.security.UserDetailsImpl;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @AuthUser 가 붙어있고, 타입이 Long인 경우에만 처리
        return parameter.getParameterAnnotation(AuthUser.class) != null
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) throws Exception {
        // 직접 인증 객체 꺼내오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 2. 인증 정보가 없거나 UserDetailsImpl 타입이 아니면
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
            boolean required = (authUser == null || authUser.required());
            if (required) {
                // 로그인 필요
                throw new CustomException(ErrorCode.MISSING_TOKEN);
            }
            return null; // optional이면 null 반환
        }
        return userDetails.getUser().getId(); // 사용자 ID 리턴
    }
}
