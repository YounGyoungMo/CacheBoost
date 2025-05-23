package com.example.CacheBoost.domain.book.dto.ResponseDto;

import com.example.CacheBoost.domain.book.entity.Book;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetBookListResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String name;

    private final Long price;

    private final String publisher;

    private final String author;

    private final String status;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static GetBookListResponseDto toDto(Book book) {
        return new GetBookListResponseDto(
            book.getId(),
            book.getName(),
            book.getPrice(),
            book.getPublisher(),
            book.getAuthor(),
            book.getStatus().name(),
            book.getCreatedAt(),
            book.getUpdatedAt()
        );
    }

}
