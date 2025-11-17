package dev.hyh.template.common.exception.custom;

/** 해킹 공격이 의심되는 예외입니다 주로 클라이언트가 잘못된 데이터를 전송했을 때 사용됩니다. HTTP 상태 코드 400 (Bad Request)에 대응합니다. */
public class HackingAttemptException extends CustomRuntimeException {
    public HackingAttemptException(String message, String code) {
        super(message, code);
    }
}
