package dev.hyh.template.common.exception.custom;

import dev.hyh.template.common.response.ErrorCode.ServerExceptionCode;
import lombok.Getter;

/**
 * 비즈니스 예외 처리를 위한 커스텀 런타임 예외 클래스입니다.
 *
 * <p>기본 RuntimeException에 에러 코드(code) 필드를 추가하여, 예외 발생 시 고유한 코드 값을 함께 전달할 수 있도록 설계되었습니다. 이 코드는 API
 * 응답이나 로깅 시 활용할 수 있습니다.
 */
@Getter
public class CustomRuntimeException extends RuntimeException {
    private final String code;

    public CustomRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public CustomRuntimeException(String message) {
        super(message);
        this.code = ServerExceptionCode.INTERNAL_SERVER_ERROR.getCode();
    }
}
