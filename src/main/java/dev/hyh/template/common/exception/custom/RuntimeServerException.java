package dev.hyh.template.common.exception.custom;

import dev.hyh.template.common.response.ErrorCode.ServerExceptionCode;

/**
 * 서버 내부에서 예기치 못한 오류가 발생했을 때 던져지는 예외입니다. 주로 서버 로직 문제, 데이터베이스 오류, 시스템 장애 등으로 인해 발생합니다. HTTP 상태 코드 500
 * (Internal Server Error)에 대응합니다.
 */
public class RuntimeServerException extends CustomRuntimeException {
    public RuntimeServerException(String message, String code) {
        super(message, code);
    }

    public RuntimeServerException(String message) {
        super(message, ServerExceptionCode.INTERNAL_SERVER_ERROR.getCode());
    }
}