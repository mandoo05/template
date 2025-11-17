package dev.hyh.template.common.exception.custom;

import dev.hyh.template.common.response.ErrorCode.RequestExceptionCode;

/**
 * 요청한 데이터를 찾지 못했을 때 발생하는 예외 클래스입니다. 주로 리소스가 존재하지 않거나 삭제된 경우에 사용됩니다. HTTP 상태 코드 404 (Not Found)에
 * 대응합니다.
 */
public class NotFoundDataException extends CustomRuntimeException {
    public NotFoundDataException(String message, String code) {
        super(message, code);
    }

    public NotFoundDataException(String message) {
        super(message, RequestExceptionCode.NOT_FOUND.getCode());
    }
}
