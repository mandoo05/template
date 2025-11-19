package dev.hyh.template.common.response.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestExceptionCode implements ErrorCodeType {
    // 000번대: 클라이언트 요청 자체의 문제
    INVALID_PARAMETER("REQ000"), // 요청 파라미터가 유효하지 않음
    MISSING_PARAMETER("REQ001"), // 필수 파라미터가 누락됨
    INVALID_FORMAT("REQ002"), // 요청 형식이 잘못됨
    DUPLICATE_REQUEST("REQ003"), // 중복된 요청입니다.
    UNSUPPORTED_MEDIA_TYPE("REQ004"), // 지원하지 않는 미디어 타입입니다.
    METHOD_NOT_ALLOWED("REQ005"), // 허용되지 않은 HTTP 메서드입니다.
    REQUEST_BODY_REQUIRED("REQ006"), // Request Body가 필요함
    PAYLOAD_TOO_LARGE("REQ007"), // 요청 페이로드가 너무 큼
    INVALID_PAGE_NUMBER("REQ008"), // 유효하지 않은 페이지 번호
    PAGE_NOT_FOUND("REQ009"), // 요청한 페이지가 존재하지 않음

    // 100번대: 인증 및 인가 관련 예외
    UNAUTHORIZED("REQ100"), // 인증이 필요합니다.
    FORBIDDEN("REQ101"), // 접근 권한이 없습니다.
    TOKEN_EXPIRED("REQ102"), // 인증 토큰이 만료됨
    TOKEN_INVALID("REQ103"), // 유효하지 않은 인증 토큰
    ACCESS_DENIED_BY_ROLE("REQ104"), // 역할에 따라 접근이 거부됨
    SESSION_EXPIRED("REQ105"), // 세션이 만료됨
    SESSION_MISMATCH("REQ106"), // 세션이 일치하지 않음

    // 200번대: 요청한 리소스를 찾을 수 없음
    NOT_FOUND("REQ200"), // 요청한 리소스를 찾을 수 없습니다.
    RESOURCE_GONE("REQ201"), // 리소스가 삭제되었거나 더 이상 존재하지 않음
    EMPTY_RESULT("REQ202"), // 요청 결과가 비어 있음
    ID_MISMATCH("REQ203"); // 경로 변수와 요청 본문의 ID가 일치하지 않음

    private final String code;
}
