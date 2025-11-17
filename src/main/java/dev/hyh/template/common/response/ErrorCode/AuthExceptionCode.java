package dev.hyh.template.common.response.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode {
    // 000번대 - 인증(Authentication) 관련
    AUTHENTICATION_FAILED("AUTH000"), // 인증 실패
    INVALID_CREDENTIALS("AUTH001"), // 아이디 또는 비밀번호가 잘못됨
    ACCOUNT_LOCKED("AUTH002"), // 계정이 잠겨 있음
    ACCOUNT_DISABLED("AUTH003"), // 계정 비활성화 상태
    ACCOUNT_EXPIRED("AUTH004"), // 계정 만료
    CREDENTIALS_EXPIRED("AUTH005"), // 비밀번호 만료
    MULTI_FACTOR_REQUIRED("AUTH006"), // 2차 인증 필요

    // 100번대 - 권한(Authorization) 관련
    ACCESS_DENIED("AUTH100"), // 접근 권한 없음
    SESSION_EXPIRED("AUTH101"), // 세션 만료

    // 200번대 - 토큰(Token) 관련
    TOKEN_MISSING("AUTH200"), // 인증 토큰 누락
    TOKEN_INVALID("AUTH201"), // 인증 토큰 유효하지 않음
    TOKEN_EXPIRED("AUTH202"); // 인증 토큰 만료됨

    private final String code;
}
