package com.example.CacheBoost.domain.user.dto.RequestDto;

import com.example.CacheBoost.domain.user.entity.Role;
import lombok.Getter;

@Getter
public class UserRequestDto {

    private String name;
    private String email;
    private String password;
    private Role role;
}
