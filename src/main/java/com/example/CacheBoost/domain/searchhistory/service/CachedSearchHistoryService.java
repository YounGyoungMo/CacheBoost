package com.example.CacheBoost.domain.searchhistory.service;

import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CachedSearchHistoryService {
    void saveSearchHistory(Long userId, String bookName);
    List<SearchHistoryResponseDto> getSearchHistories(Long userId);
    void removeSearchHistory(Long userId);

}
