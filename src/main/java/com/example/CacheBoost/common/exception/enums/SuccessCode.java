package com.example.CacheBoost.common.exception.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessCode {

    //
    SEARCH_BOOK_SUCCESS(HttpStatus.OK, "도서 검색 결과가 성공적으로 조회되었습니다. "),
    SUCCESS_SEARCH_RESULT_BOOK_NOT_FOUND(HttpStatus.OK, "검색 결과, 해당 도서를 찾을 수 없습니다."),
    ADD_BOOK_SUCCESS(HttpStatus.CREATED, "도서 생성이 성공적으로 추가되었습니다. "),
    UPDATE_BOOK_SUCCESS(HttpStatus.OK, "도서 정보가 성공적으로 수정되었습니다. "),
    DELETE_BOOK_SUCCESS(HttpStatus.NO_CONTENT, "도서가 성공적으로 삭제되었습니다. "),

    // 회원가입 및 로그인 성공
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "토큰 재발급에 성공하였습니다."),

    ;


    private final HttpStatus httpStatus;
    private final String message;

}
