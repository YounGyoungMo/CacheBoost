package com.example.CacheBoost.domain.searchhistory.dto.ResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchHistoryResponseDto {
    private final Long id;

    // 나중에 로그인 기능 구현되면 없애도 됨
    private final Long userId;

    private final String keyword;
}
