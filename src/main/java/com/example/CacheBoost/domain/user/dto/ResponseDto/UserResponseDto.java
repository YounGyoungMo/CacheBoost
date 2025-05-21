package com.example.CacheBoost.domain.user.dto.ResponseDto;

import com.example.CacheBoost.domain.user.entity.Role;
import com.example.CacheBoost.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String name;
    private final String email;
    private final Role role;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
