package com.example.CacheBoost.domain.searchhistory.service;

import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;
import com.example.CacheBoost.domain.user.entity.User;

import java.util.List;

public interface SearchHistoryService {

    List<SearchHistoryResponseDto> saveSearchHistory(User user, String bookName);

    List<SearchHistoryResponseDto> getSearchHistories(Long userId);

    void removeSearchHistory(Long userId);

    List<SearchKeywordResponseDto> getSearchKeywords();
}
