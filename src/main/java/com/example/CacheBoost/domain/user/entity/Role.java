package com.example.CacheBoost.domain.user.entity;


import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    ADMIN("ADMIN_ROLE", "관리자"),
    USER("USER_ROLE", "일반 사용자");
    private final String key;
    private final String title;

    Role(String key, String title) {
        this.key = key;
        this.title = title;
    }
    // 정적 팩토리 메서드
    public static Role of(String input) {
        return Arrays.stream(Role.values())
                .filter(role ->
                        role.name().equalsIgnoreCase(input) || // role.name() == enum상수 ADMIN,USER
                                role.key.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ROLE));
    }

    @Override
    public String toString() {
        return title;
    }
}
