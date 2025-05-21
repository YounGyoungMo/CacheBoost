package com.example.CacheBoost.domain.auth;


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {
    boolean required() default true;  // ← 이거 추가!
}
