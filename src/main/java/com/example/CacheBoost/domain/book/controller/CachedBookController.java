package com.example.CacheBoost.domain.book.controller;

import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.auth.AuthUser;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetBookListResponseDto;
import com.example.CacheBoost.domain.book.service.BookService;
import com.example.CacheBoost.domain.searchhistory.service.SearchHistoryService;
import com.example.CacheBoost.domain.searchkeyword.service.SearchKeywordService;
import com.example.CacheBoost.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class CachedBookController {

    private final BookService bookService;
    private final SearchHistoryService searchHistoryService;
    private final SearchKeywordService searchKeywordService;

    //
    public CachedBookController(
            BookService bookService,
            @Qualifier("searchHistoryCacheService") SearchHistoryService searchHistoryService,
            @Qualifier("searchKeywordCacheService") SearchKeywordService searchKeywordService
    ) {
        this.bookService = bookService;
        this.searchHistoryService = searchHistoryService;
        this.searchKeywordService = searchKeywordService;
    }

    @GetMapping("/api/v2/books/search")
    public ResponseEntity<ApiResponseDto<List<GetBookListResponseDto>>> searchBooks(
            @AuthUser Long userId,
            @RequestParam String bookName) {

        List<GetBookListResponseDto> searchBooks = bookService.findAllByBookName(bookName);

        // 검색 기록 저장 서비스 호출
        // 원래는 조회된 도서에 검색 기록까지 같이 반환하도록 하려 했지만 일단 보류 (도서와 검색 기록을 같이 반환하는 DTO를 따로 설계해야함)
        searchHistoryService.saveSearchHistory(userId, bookName);

        // 인기 검색어 집계 서비스 호출
        searchKeywordService.saveSearchKeyword(bookName);

        if (searchBooks.isEmpty()) {
            return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.SUCCESS_SEARCH_RESULT_BOOK_NOT_FOUND, searchBooks));
        } else {
            return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.SEARCH_BOOK_SUCCESS, searchBooks));
        }

    }


}
