package com.example.CacheBoost.domain.searchhistory.controller;

import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.auth.AuthUser;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchhistory.service.CachedSearchHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class CachedSearchHistoryController {

    private final CachedSearchHistoryServiceImpl cachedSearchHistoryService;

    public CachedSearchHistoryController(
            @Qualifier("searchHistoryCacheService") CachedSearchHistoryServiceImpl cachedSearchHistoryService
            ) {

        this.cachedSearchHistoryService = cachedSearchHistoryService;
    }

    @GetMapping("/search-history")
    public ResponseEntity<ApiResponseDto<List<SearchHistoryResponseDto>>> getSearchHistories(
            @AuthUser Long userId
    ) {
        List<SearchHistoryResponseDto> results = cachedSearchHistoryService.getSearchHistories(userId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.SEARCH_HISTORY_SUCCESS, results));
    }

    @DeleteMapping("/search-history")
    public ResponseEntity<ApiResponseDto<List<SearchHistoryResponseDto>>> removeSearchHistories(
            @AuthUser Long userId
    ) {
        cachedSearchHistoryService.removeSearchHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.DELETE_ALL_SEARCH_HISTORY_SUCCESS));
    }
}
