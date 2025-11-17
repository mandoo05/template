package dev.hyh.template.common.response.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerExceptionCode {
    // 000번대: 일반 서버 오류
    INTERNAL_SERVER_ERROR("SRV000"), // 알 수 없는 서버 오류
    SERVICE_UNAVAILABLE("SRV001"), // 서비스 일시 중단 또는 준비되지 않음
    UNKNOWN_EXCEPTION("SRV002"), // 정의되지 않은 예외 발생
    CONFIGURATION_ERROR("SRV003"), // 서버 설정 오류

    // 100번대: DB 관련 예외
    DATABASE_ERROR("SRV100"), // 일반적인 DB 오류
    DUPLICATE_KEY("SRV101"), // 중복 키 제약 위반
    CONSTRAINT_VIOLATION("SRV102"), // DB 제약 조건 위반
    DATA_INTEGRITY_ERROR("SRV103"), // 데이터 무결성 오류
    QUERY_TIMEOUT("SRV104"), // 쿼리 실행 시간 초과
    DEADLOCK_DETECTED("SRV105"), // 교착 상태 발생
    INTERRUPTED("SRV106"), // 스레드 인터럽트 발생

    // 200번대: 파일/IO/서버 리소스
    FILE_IO_ERROR("SRV200"), // 파일 입출력 오류
    FILE_NOT_FOUND("SRV201"), // 파일을 찾을 수 없음
    STORAGE_FULL("SRV202"), // 저장 공간 부족
    MEMORY_OVERLOAD("SRV203"); // 메모리 과부하

    private final String code;
}
