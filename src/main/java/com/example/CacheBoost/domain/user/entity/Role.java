package com.example.CacheBoost.domain.user.entity;


import lombok.Getter;

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

    @Override
    public String toString() {
        return title;
    }
}
