package com.example.CacheBoost.domain.searchhistory.service;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;

import java.util.List;

public interface SearchHistoryService {

    void saveSearchHistory(Long userId, String bookName);

    List<SearchHistoryResponseDto> getSearchHistories(Long userId);

    void removeSearchHistory(Long userId);
}
