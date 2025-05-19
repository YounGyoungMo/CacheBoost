package com.example.CacheBoost.domain.book.dto.ResponseDto;

import com.example.CacheBoost.domain.book.entity.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateBookResponseDto {

    private final Long id;

    private final Long price;

    public static UpdateBookResponseDto toDto(Book book) {
        return new UpdateBookResponseDto(
            book.getId(),
            book.getPrice()
        );
    }

}
