package com.example.CacheBoost.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 도서
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 도서가 존재하지 않습니다."),
    BOOK_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "삭제된 도서는 수정할 수 없습니다."),
    INVALID_BOOK_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 도서입니다."),

    // 검색
    SEARCH_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "검색기록이 존재하지 않습니다."),
    SEARCH_KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "인기 검색어가 존재하지 않습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),

    // Role
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 역할입니다."),

    // Redis 연결 실패
    REDIS_CONNECTION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 연결에 실패했습니다."),

    // 인증
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "이미 로그아웃된 토큰입니다."),
    EMPTY_JWT(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    INVALID_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 로그인 요청입니다. 요청 포맷을 확인해주세요."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    EXTEND_LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "AccessToken은 최대 3회까지만 연장할 수 있습니다."),
    TOO_EARLY_TO_EXTEND(HttpStatus.BAD_REQUEST, "AccessToken은 만료 5분 이내일 때만 연장할 수 있습니다."),
    ALREADY_AUTHENTICATED(HttpStatus.CONFLICT, "ALREADY_AUTHENTICATED"),

    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "해당 주소에 대한 접근 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
