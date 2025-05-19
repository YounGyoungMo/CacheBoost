package com.example.CacheBoost.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorData {
    private final String path;
    private final LocalDateTime timestamp;
}
