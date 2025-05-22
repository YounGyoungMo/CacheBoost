package com.example.CacheBoost.domain.searchhistory.controller;

import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.auth.AuthUser;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchhistory.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class CachedSearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    public CachedSearchHistoryController(
            @Qualifier("searchHistoryCacheService") SearchHistoryService searchHistoryService
            ) {

        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping("/search-history")
    public ResponseEntity<ApiResponseDto<List<SearchHistoryResponseDto>>> getCachedSearchHistories(@AuthUser Long userId) {
        List<SearchHistoryResponseDto> cachedSearchHistories = searchHistoryService.getSearchHistories(userId);

        SuccessCode status = cachedSearchHistories.isEmpty() ? SuccessCode.NO_SEARCH_HISTORY : SuccessCode.SEARCH_HISTORY_SUCCESS;

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponseDto.success(status, cachedSearchHistories));
    }

    @DeleteMapping("/search-history")
    public ResponseEntity<ApiResponseDto<List<SearchHistoryResponseDto>>> removeSearchHistories(
            @AuthUser Long userId
    ) {
        searchHistoryService.removeSearchHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.DELETE_ALL_SEARCH_HISTORY_SUCCESS));
    }
}
