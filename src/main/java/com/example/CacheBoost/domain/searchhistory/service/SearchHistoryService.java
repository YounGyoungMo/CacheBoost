package com.example.CacheBoost.domain.searchhistory.service;


import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;

import java.util.List;

public interface SearchHistoryService {

    List<SearchHistoryResponseDto> saveSearchHistory(Long userId, String bookName);

    List<SearchHistoryResponseDto> getSearchHistories(Long userId);

    void removeSearchHistory(Long userId);

    List<SearchKeywordResponseDto> getSearchKeywords();
}
