package dev.hyh.template.common.exception.custom;

/**
 * 인증은 되었으나 해당 리소스에 대한 접근 권한이 없는 경우 발생하는 예외입니다. 주로 권한 부족으로 인한 접근 거부 상황에 사용됩니다. HTTP 상태 코드 403
 * (Forbidden)에 대응합니다.
 */
public class ForbiddenException extends CustomRuntimeException {
    public ForbiddenException(String message, String code) {
        super(message, code);
    }
}
