package com.example.CacheBoost.common.exception.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessCode {

    //
    SEARCH_SUCCESS(HttpStatus.OK, "도서 검색 결과가 성공적으로 조회되었습니다. "),

    ;


    private final HttpStatus httpStatus;
    private final String message;

}
