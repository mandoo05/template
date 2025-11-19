package dev.hyh.template.common.response.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ErrorCodeType {
    // 000번대: 사용자에게 문제가 없으나 요청 처리 중 문제가 발생한 경우
    USER_NOT_FOUND("USR001"), // 사용자를 찾을 수 없음
    DUPLICATE_USER("USR002"), // 이미 존재하는 사용자
    USER_ALREADY_DELETED("USR003"), // 이미 탈퇴한 사용자
    USER_INACTIVE("USR004"), // 비활성화된 사용자

    // 100번대: 계정 상태 관련 예외
    ACCOUNT_LOCKED("USR100"), // 계정이 잠겨 있음
    ACCOUNT_DISABLED("USR101"), // 계정이 비활성화됨
    ACCOUNT_SUSPENDED("USR102"), // 계정이 일시 정지됨
    ACCOUNT_BANNED("USR103"); // 계정이 영구 정지됨

    private final String code;
}