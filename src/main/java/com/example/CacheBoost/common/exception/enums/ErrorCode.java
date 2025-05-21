package com.example.CacheBoost.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 도서가 존재하지 않습니다."),
    BOOK_ALREADY_DELETED(HttpStatus.BAD_REQUEST,"삭제된 도서는 수정할 수 없습니다."),
    INVALID_BOOK_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 도서입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
