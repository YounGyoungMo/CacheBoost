package com.example.CacheBoost.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 책이 존재하지 않습니다."),
    BOOK_ALREADY_DELETED(HttpStatus.BAD_REQUEST,"삭제된 메뉴는 수정할 수 없습니다."),
    INVALID_BOOK_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 도서입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
