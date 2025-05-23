package com.example.CacheBoost.domain.auth.dto;

import com.example.CacheBoost.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenPayload {
    private Long userId;
    private String email;
    private Role role;
}
