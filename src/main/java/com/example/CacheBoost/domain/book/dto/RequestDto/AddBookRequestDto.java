package com.example.CacheBoost.domain.book.dto.RequestDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddBookRequestDto {

    private final String name;

    private final Long price;

    private final String publisher;

    private final String publishedDade;

    private final String isbn;

    private final String author;

    private final String description;

}
