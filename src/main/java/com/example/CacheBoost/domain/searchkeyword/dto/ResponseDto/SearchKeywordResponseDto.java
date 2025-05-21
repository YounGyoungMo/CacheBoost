package com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto;

import com.example.CacheBoost.domain.searchkeyword.entity.SearchKeyword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SearchKeywordResponseDto {
    private final Long id;
    private final String keyword;
    private final Long searchCnt;
    private final LocalDateTime createdAt;

    public static SearchKeywordResponseDto from(SearchKeyword searchKeyword) {
        return new SearchKeywordResponseDto(searchKeyword.getId(), searchKeyword.getKeyword(), searchKeyword.getSearchCnt(), searchKeyword.getCreatedAt());
    }
}
