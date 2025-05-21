package com.example.CacheBoost.domain.book.controller;

import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.book.dto.RequestDto.AddBookRequestDto;
import com.example.CacheBoost.domain.book.dto.RequestDto.UpdateBookRequestDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.AddBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetBookListResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetSingleBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.UpdateBookResponseDto;
import com.example.CacheBoost.domain.book.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class BookController {

    private final BookService bookService;

    @PostMapping("/admin/books")
    public ResponseEntity<ApiResponseDto<AddBookResponseDto>> addBook(
        @RequestBody AddBookRequestDto requestDto) {

        AddBookResponseDto bookResponseDto = bookService.addBook(requestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.ADD_BOOK_SUCCESS, bookResponseDto));
    }

    @GetMapping("/api/v1/books/search")
    public ResponseEntity<ApiResponseDto<List<GetBookListResponseDto>>> searchBooks(
        @RequestParam String bookName) {

        List<GetBookListResponseDto> searchBooks = bookService.findAllByBookName(bookName);

        if (searchBooks.isEmpty()){
            return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.SUCCESS_SEARCH_RESULT_BOOK_NOT_FOUND, searchBooks));
        } else {
            return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.SEARCH_BOOK_SUCCESS, searchBooks));
        }

    }

    @GetMapping("/api/v1/books/{bookId}")
    public ResponseEntity<ApiResponseDto<GetSingleBookResponseDto>> findBookBy(@PathVariable Long bookId) {

        GetSingleBookResponseDto getSingleBookResponseDto = bookService.findBookBy(bookId);

        return ResponseEntity.ok(
            ApiResponseDto.success(SuccessCode.SEARCH_BOOK_SUCCESS, getSingleBookResponseDto));
    }

    @PatchMapping("/admin/books/{bookId}")
    public ResponseEntity<ApiResponseDto<UpdateBookResponseDto>> updateBook(
        @PathVariable Long bookId,
        @RequestBody UpdateBookRequestDto requestDto) {

        UpdateBookResponseDto updateBookResponseDto = bookService.updateBook(bookId, requestDto);

        return ResponseEntity.ok(
            ApiResponseDto.success(SuccessCode.UPDATE_BOOK_SUCCESS, updateBookResponseDto));
    }

    @DeleteMapping("/admin/books/{bookId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteBook(@PathVariable Long bookId) {

        bookService.deleteBook(bookId);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DELETE_BOOK_SUCCESS));
    }
}
