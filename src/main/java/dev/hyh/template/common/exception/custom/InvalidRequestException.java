package dev.hyh.template.common.exception.custom;

/**
 * 요청 형식은 올바르나(유효성 검사에는 통과했지만) 실제로는 유효하지 않은(논리적으로 맞지 않는) 정보가 포함된 경우 발생하는 예외입니다. 주로 클라이언트가 잘못된 데이터를
 * 전송했을 때 사용됩니다. HTTP 상태 코드 400 (Bad Request)에 대응합니다.
 */
public class InvalidRequestException extends CustomRuntimeException {
    public InvalidRequestException(String message, String code) {
        super(message, code);
    }
}
