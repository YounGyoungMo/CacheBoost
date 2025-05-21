package com.example.CacheBoost.domain.searchhistory.controller;

import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.auth.AuthUser;
import com.example.CacheBoost.domain.searchhistory.dto.ResponseDto.SearchHistoryResponseDto;
import com.example.CacheBoost.domain.searchhistory.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.CacheBoost.common.exception.enums.SuccessCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchService;

    @GetMapping("/api/v1/search-history")
    public ResponseEntity<ApiResponseDto<List<SearchHistoryResponseDto>>> getSearchHistories(
            @AuthUser Long userId
    ) {
        List<SearchHistoryResponseDto> searchHistories = searchService.getSearchHistories(userId);

        SuccessCode status = searchHistories.isEmpty() ? SuccessCode.NO_SEARCH_HISTORY : SuccessCode.SEARCH_HISTORY_SUCCESS;

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDto.success(status, searchHistories));
    }

    @DeleteMapping("/api/v1/search-history")
    public ResponseEntity<ApiResponseDto<List<SearchHistoryResponseDto>>> removeSearchHistories(
            @AuthUser Long userId
    ) {
        searchService.removeSearchHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(SuccessCode.SEARCH_HISTORY_DELETE_SUCCESS));
    }


}
