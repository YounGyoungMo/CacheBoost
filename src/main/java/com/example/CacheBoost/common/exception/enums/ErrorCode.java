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

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),

    // Role
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 역할입니다."),

    // 인증
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰입니다."),
    EMPTY_JWT(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    INVALID_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 로그인 요청입니다. 요청 포맷을 확인해주세요."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
