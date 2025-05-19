package com.example.CacheBoost.domain.book.controller;

import com.example.CacheBoost.domain.book.dto.RequestDto.AddBookRequestDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.BookResponseDto;
import com.example.CacheBoost.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class BookController {

    private final BookService bookService;

    @PostMapping("/admin/books")
    public ResponseEntity<BookResponseDto> addBook(@RequestBody AddBookRequestDto requestDto) {
        BookResponseDto bookResponseDto = bookService.addBook(requestDto);
    }
}
