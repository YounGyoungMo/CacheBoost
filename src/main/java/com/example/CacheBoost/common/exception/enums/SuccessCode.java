package com.example.CacheBoost.common.exception.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessCode {

    // 도서 관련
    SEARCH_BOOK_SUCCESS(HttpStatus.OK, "도서 검색 결과가 성공적으로 조회되었습니다. "),
    SUCCESS_SEARCH_RESULT_BOOK_NOT_FOUND(HttpStatus.OK, "검색 결과, 해당 도서를 찾을 수 없습니다."),
    ADD_BOOK_SUCCESS(HttpStatus.CREATED, "도서 생성이 성공적으로 추가되었습니다. "),
    UPDATE_BOOK_SUCCESS(HttpStatus.OK, "도서 정보가 성공적으로 수정되었습니다. "),
    DELETE_BOOK_SUCCESS(HttpStatus.NO_CONTENT, "도서가 성공적으로 삭제되었습니다. "),
    
    // 검색 관련
    SEARCH_HISTORY_SUCCESS(HttpStatus.OK, "검색 기록이 성공적으로 조회되었습니다. " ),
    SEARCH_KEYWORD_SUCCESS(HttpStatus.OK, "인기 검색어가 성공적으로 조회되었습니다. " ),

    // 도서 검색 결과가 존재하지 않는데도 성공 코드로 분류한 이유
    // 검색 요청 자체는 유효하며 단순히 데이터가 없는 정상적인 결과이므로 예외 상황으로 간주하지 않고 성공 메시지를 통해 의미를 전달
    NO_SEARCH_HISTORY(HttpStatus.OK, "도서 검색 결과가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
