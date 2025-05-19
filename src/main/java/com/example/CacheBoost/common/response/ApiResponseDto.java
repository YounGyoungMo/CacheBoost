package com.example.CacheBoost.common.response;

import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.NonNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
public record ApiResponseDto<T>( // record: dto를 간결하게 작성하는 방식으로 생성자/getter/equals 를 자동으로 만들어줌, 불변객체임// 요청 시각
                                 int statusCode,                                // HTTP 상태 코드 숫자 (예: 400)
                                 @NonNull String message,                       // 응답 메시지 (ex: “해당 도서가 성공적으로 조회되었습니다. ", "잘못된 요청입니다")
                                 @JsonInclude(value = NON_NULL) T data// 실제 응답 데이터 (nullable), null이면 JSON에서 제외됨
) {
    /**
     * 성공 응답을 생성하는 메소드
     * 예를 들어 검색 조회에 성공했을 때 데이터도 함께 반환
     * @param successCode 성공 상태코드/메시지 Enum
     * @param data 응답 데이터
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode,
                                                @Nullable final T data
    ) {
        return new ApiResponseDto<>(
                successCode.getHttpStatus().value(),
                successCode.getMessage(),
                data
        );
    }

    /**
     * 성공 응답을 생성하는 메소드
     * DELETE 등 응답을 성공했을 때 반환되는 데이터가 없을 때 사용
     * @param successCode 성공 상태코드/메시지 Enum
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode) {
        return new ApiResponseDto<>(
            successCode.getHttpStatus().value(),
            successCode.getMessage(),
            null
        );
    }

    /**
     * 실패 응답을 생성하는 메소드 (ErrorCode 기반)
     * 예를 들어 검색 조회에 실패했을 때 데이터에 path와 timestamp를 포함시켜서 같이 조회
     * @param errorCode 에러 코드 Enum
     * @param path     요청 path
     * @return ApiResponseDto
     */
    public static ApiResponseDto<ErrorData> fail(final ErrorCode errorCode, final String path) {
        return new ApiResponseDto<>(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                new ErrorData(path, LocalDateTime.now())
        );
    }

}
