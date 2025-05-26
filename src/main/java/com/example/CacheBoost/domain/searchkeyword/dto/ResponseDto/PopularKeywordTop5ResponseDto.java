package com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PopularKeywordTop5ResponseDto {

    private final String keyword;
    private final Double score;

}
