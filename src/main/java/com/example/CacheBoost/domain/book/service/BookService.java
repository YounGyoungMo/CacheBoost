package com.example.CacheBoost.domain.book.service;

import com.example.CacheBoost.common.aop.ExecutionTimer;
import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.book.dto.RequestDto.AddBookRequestDto;
import com.example.CacheBoost.domain.book.dto.RequestDto.UpdateBookRequestDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.AddBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetBookListResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.GetSingleBookResponseDto;
import com.example.CacheBoost.domain.book.dto.ResponseDto.UpdateBookResponseDto;
import com.example.CacheBoost.domain.book.entity.Book;
import com.example.CacheBoost.domain.book.entity.Status;
import com.example.CacheBoost.domain.book.repository.BookRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    //  사용할 스레드 개수 8개
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    @Transactional
    public AddBookResponseDto addBook(AddBookRequestDto requestDto) {

        // 권한 검증

        // 도서 저장
        Book book = bookRepository.save(new Book(requestDto));

        return AddBookResponseDto.toDto(book);
    }

    @ExecutionTimer("캐시를 이용한 도서 검색 실행 시간")
    @Cacheable(value = "searchBooks", key = "#bookName")
    public List<GetBookListResponseDto> findAllByBookNameWithCache(String bookName) {
        log.info("🔍 DB에서 검색 실행: {}", bookName);
        return bookRepository.findAllByBookName(bookName)
                .stream()
                .map(GetBookListResponseDto::toDto)
                .toList();
    }
    @ExecutionTimer("도서 검색 실행 시간")
    public List<GetBookListResponseDto> findAllByBookName(String bookName) {
        log.info("🔍 DB에서 검색 실행: {}", bookName);
        return bookRepository.findAllByBookName(bookName)
                .stream()
                .map(GetBookListResponseDto::toDto)
                .toList();
    }



    public GetSingleBookResponseDto findBookBy(Long bookId) {

        // 도서 조회
        Book book = bookRepository.findByIdOrElseThrow(bookId);

        // 삭제된 도서 검증
        if (book.isDeleted()) {
            throw new CustomException(ErrorCode.INVALID_BOOK_ID);
        }

        return GetSingleBookResponseDto.toDto(book);
    }

    @Transactional
    public UpdateBookResponseDto updateBook(Long bookId, UpdateBookRequestDto requestDto) {

        // 권한 검증

        // 도서 조회
        Book book = bookRepository.findByIdOrElseThrow(bookId);

        // 삭제된 도서 검증
        if (book.isDeleted()) {
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
    public void generateDummyBooksParallel(int totalCount) {
        int batchSize = 5000;

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < totalCount; i += batchSize) {
            final int start = i;

            futures.add(
                    CompletableFuture.runAsync(() -> insertBatch(start, batchSize, totalCount), executor)
            );
        }

        // 모든 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBatch(int start, int batchSize, int totalCount) {
        List<Book> books = new ArrayList<>();

        for (int i = start; i < start + batchSize && i < totalCount; i++) {
            books.add(Book.builder()
                    .name("병렬 자바의 정석 " + i)
                    .price(10000L)
                    .publisher("멀티출판사")
                    .publishedDate(LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .isbn("ISBN-" + UUID.randomUUID())
                    .author("이재홍")
                    .description("병렬 생성용")
                    .status(Status.ACTIVE)
                    .build());
        }

        bookRepository.saveAll(books);
    }

    @Transactional
    public void generateDummyBooks(int totalCount) {
        int batchSize = 1000;
        for (int i = 0; i < totalCount; i += batchSize) {
            List<Book> books = new ArrayList<>();
            for (int j = 0; j < batchSize && i + j < totalCount; j++) {
                books.add(Book.builder()
                        .name("자바의 정석 v " + i + "회 " + j + "번")
                        .price((long) (10000 + j))
                        .publisher("더미출판사")
                        .publishedDate(LocalDate.now().minusDays(j).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .isbn("ISBN-" + UUID.randomUUID())
                        .author("이재홍")
                        .description("코딩을 처음 배우는 사람도 자바를 쉽게 배울수 있게 도와준다.")
                        .status(Status.ACTIVE)
                        .build());
            }
            bookRepository.saveAll(books);
            // flush()로 계속 DB에 저장
            bookRepository.flush();
        }

    }
}
