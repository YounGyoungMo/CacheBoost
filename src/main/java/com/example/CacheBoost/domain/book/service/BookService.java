package com.example.CacheBoost.domain.book.service;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.book.dto.RequestDto.AddBookRequestDto;
import com.example.CacheBoost.domain.book.dto.RequestDto.UpdateBookRequestDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.BookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.DeleteBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.UpdateBookResponseDto;
import com.example.CacheBoost.domain.book.entity.Book;
import com.example.CacheBoost.domain.book.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookResponseDto addBook(AddBookRequestDto requestDto) {

        // 권한 검증

        // 도서 저장
        Book book = bookRepository.save(new Book(requestDto));

        return BookResponseDto.toDto(book);
    }

    public List<BookResponseDto> findAllByBookName(String bookName) {
        return bookRepository.findByAllBookName(bookName)
            .stream()
            .map(BookResponseDto::toDto)
            .toList();
    }


    public BookResponseDto findBookBy(Long bookId) {

        // 도서 조회
        Book book = bookRepository.findByIdOrElseThrow(bookId);

        // 삭제된 도서 검증
        if(book.isDeleted()){
            throw new CustomException(ErrorCode.INVALID_BOOK_ID);
        }

        return BookResponseDto.toDto(book);
    }

    @Transactional
    public UpdateBookResponseDto updateBook(Long bookId, UpdateBookRequestDto requestDto) {

        // 권한 검증

        // 도서 조회
        Book book = bookRepository.findByIdOrElseThrow(bookId);

        // 삭제된 도서 검증
        if(book.isDeleted()){
            throw new CustomException(ErrorCode.BOOK_ALREADY_DELETED);
        }

        // 도서 수정
        book.updateBook(requestDto);

        return UpdateBookResponseDto.toDto(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {

        // 권한 검증

        // 도서 조회
        Book book = bookRepository.findByIdOrElseThrow(bookId);

        book.deleteBook();

    }
}
