package com.example.CacheBoost.domain.book.dto.ResponseDto;

import com.example.CacheBoost.domain.book.entity.Book;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetSingleBookResponseDto {

    private final Long id;

    private final String name;

    private final Long price;

    private final String publisher;

    private final String publishedData;

    private final String isbn;

    private final String author;

    private final String description;

    private final String status;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static GetSingleBookResponseDto toDto(Book book) {
        return new GetSingleBookResponseDto(
            book.getId(),
            book.getName(),
            book.getPrice(),
            book.getPublisher(),
            book.getPublishedDate(),
            book.getIsbn(),
            book.getAuthor(),
            book.getDescription(),
            book.getStatus().name(),
            book.getCreatedAt(),
            book.getUpdatedAt()
        );
    }

}
