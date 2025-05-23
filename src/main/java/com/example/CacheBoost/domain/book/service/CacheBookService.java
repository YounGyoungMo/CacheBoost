package com.example.CacheBoost.domain.book.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.book.dto.RequestDto.AddBookRequestDto;
import com.example.CacheBoost.domain.book.dto.RequestDto.UpdateBookRequestDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.AddBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetBookListResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetSingleBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.UpdateBookResponseDto;
import com.example.CacheBoost.domain.book.entity.Book;
import com.example.CacheBoost.domain.book.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CacheBookService {

    private final BookRepository bookRepository;

    @Cacheable(value = "searchBook", key = "#bookName")
    public List<GetBookListResponseDto> findAllByBookName(String bookName) {
        return bookRepository.findAllByBookName(bookName)
            .stream()
            .map(GetBookListResponseDto::toDto)
            .toList();
    }

    @Cacheable(value = "searchBook", key = "#bookId")
    public GetSingleBookResponseDto findBookBy(Long bookId) {

        // 도서 조회
        Book book = bookRepository.findByIdOrElseThrow(bookId);

        // 삭제된 도서 검증
        if(book.isDeleted()){
            throw new CustomException(ErrorCode.INVALID_BOOK_ID);
        }

        return GetSingleBookResponseDto.toDto(book);
    }

}
