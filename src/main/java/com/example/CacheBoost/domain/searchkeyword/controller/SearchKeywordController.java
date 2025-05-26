package com.example.CacheBoost.domain.searchkeyword.controller;

import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.PopularKeywordTop5ResponseDto;
import com.example.CacheBoost.domain.searchkeyword.dto.ResponseDto.SearchKeywordResponseDto;
import com.example.CacheBoost.domain.searchkeyword.service.CacheSearchKeywordService;
import com.example.CacheBoost.domain.searchkeyword.service.SearchKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.CacheBoost.common.exception.enums.SuccessCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchKeywordController {
    private final SearchKeywordService searchKeywordService;
    private final CacheSearchKeywordService cacheSearchKeywordService;
    
    // 인기 검색어 조회
    @GetMapping("/api/v1/keywords/popular")
    public ResponseEntity<ApiResponseDto<List<SearchKeywordResponseDto>>> getSearchKeywords() {
        List<SearchKeywordResponseDto> searchKeywords = searchKeywordService.getSearchKeywords();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDto.success(SuccessCode.SEARCH_KEYWORD_SUCCESS, searchKeywords));
    }

    @GetMapping("/api/v2/keywords/popular")
    public ResponseEntity<ApiResponseDto<List<PopularKeywordTop5ResponseDto>>> getSearchKeywordsV2() {
        List<PopularKeywordTop5ResponseDto> searchKeywords = cacheSearchKeywordService.getSearchKeywords();
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponseDto.success(SuccessCode.SEARCH_KEYWORD_SUCCESS, searchKeywords));
    }

}
