package com.example.CacheBoost.domain.searchkeyword.service;

import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;

import java.util.List;

public interface SearchKeywordService {
    List<SearchKeywordResponseDto> getSearchKeywords();

    void saveSearchKeyword(String bookName);
}
