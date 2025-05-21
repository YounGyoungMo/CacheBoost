package com.example.CacheBoost.domain.book.entity;

import com.example.CacheBoost.common.entity.BaseTimeEntity;
import com.example.CacheBoost.domain.book.dto.RequestDto.AddBookRequestDto;
import com.example.CacheBoost.domain.book.dto.RequestDto.UpdateBookRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "book")
@Entity
@Getter
@NoArgsConstructor
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String publishedData;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    public Book(AddBookRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.publisher = requestDto.getPublisher();
        this.publishedData = requestDto.getPublishedDate();
        this.isbn = requestDto.getIsbn();
        this.author = requestDto.getAuthor();
        this.description = requestDto.getDescription();
    }

    public void updateBook(UpdateBookRequestDto requestDto) {
        this.price = requestDto.getPrice();
    }

    public void deleteBook() {
        this.status = Status.DELETED;
    }

    public boolean isDeleted() {
        return status != Status.ACTIVE;
    }
}
