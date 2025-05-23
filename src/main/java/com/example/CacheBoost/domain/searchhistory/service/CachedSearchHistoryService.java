package com.example.CacheBoost.domain.searchhistory.service;

import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CachedSearchHistoryService {
    void saveSearchHistory(Long userId, String bookName);
    Page<SearchHistoryResponseDto> getSearchHistories(Long userId, String keyword, Pageable pageable);
    void removeSearchHistory(Long userId);

}
