package com.example.CacheBoost.common.exception.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessCode {

    //
    SEARCH_SUCCESS(HttpStatus.OK, "도서 검색 결과가 성공적으로 조회되었습니다. "),
    ADD_SUCCESS(HttpStatus.CREATED, "도서 생성이 성공적으로 추가되었습니다. "),
    UPDATE_SUCCESS(HttpStatus.OK, "도서 정보가 성공적으로 수정되었습니다. "),
    DELETE_SUCCESS(HttpStatus.NO_CONTENT, "도서가 성공적으로 삭제되었습니다. "),

    ;


    private final HttpStatus httpStatus;
    private final String message;

}
