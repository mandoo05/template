package dev.hyh.template.common.response.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 성공
    SUCCESS(200, null),

    // 클라이언트 에러 4xx
    BAD_REQUEST(400, "잘못된 요청입니다."),
    UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(409, "중복된 데이터입니다."),

    // 인증/인가 관련
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    INVALID_CREDENTIALS(401, "아이디 또는 비밀번호가 일치하지 않습니다."),

    // 사용자 관련
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(409, "이미 존재하는 이메일입니다."),
    DUPLICATE_USERNAME(409, "이미 존재하는 사용자명입니다."),
    INVALID_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),

    // 검증 에러
    INVALID_INPUT(400, "입력값이 올바르지 않습니다."),
    MISSING_REQUIRED_FIELD(400, "필수 항목이 누락되었습니다."),
    INVALID_FORMAT(400, "입력 형식이 올바르지 않습니다."),

    // 비즈니스 로직 에러
    ALREADY_PROCESSED(400, "이미 처리된 요청입니다."),
    INSUFFICIENT_BALANCE(400, "잔액이 부족합니다."),
    OPERATION_NOT_ALLOWED(400, "허용되지 않은 작업입니다."),

    // ProcessTemplate 관련 에러
    DUPLICATE_PROCESS_TEMPLATE(409, "이미 존재하는 프로세스 템플릿입니다."),
    PROCESS_ALREADY_EXISTS(409, "이미 존재하는 프로세스입니다."),
    PROCESS_TEMPLATE_NOT_FOUND(404, "프로세스 템플릿을 찾을 수 없습니다."),

    // 서버 에러 5xx
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(500, "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(500, "외부 API 호출 중 오류가 발생했습니다.");

    private final Integer code;
    private final String message;
}
