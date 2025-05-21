package com.example.CacheBoost.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    // accessToken이랑 refreshToken중 어떻게 처리해야 될까 일단 accessToken만
    private String accessToken;
//    private String refreshToken;
}
