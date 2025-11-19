package dev.hyh.template.common.swagger;

import dev.hyh.template.common.response.ErrorCode.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Tag(name = "Error", description = "Error Code 문서 그룹")
@RequestMapping("/docs/error")
public class ErrorCodeController {

    @Operation(summary = "Auth 에러 코드 목록")
    @GetMapping("/auth")
    public List<ErrorCodeResponse> authCodes() {
        return toList(AuthExceptionCode.values());
    }

    @Operation(summary = "Request 에러 코드 목록")
    @GetMapping("/request")
    public List<ErrorCodeResponse> requestCodes() {
        return toList(RequestExceptionCode.values());
    }

    @Operation(summary = "Server 에러 코드 목록")
    @GetMapping("/server")
    public List<ErrorCodeResponse> serverCodes() {
        return toList(ServerExceptionCode.values());
    }

    @Operation(summary = "User 에러 코드 목록")
    @GetMapping("/user")
    public List<ErrorCodeResponse> userCodes() {
        return toList(UserExceptionCode.values());
    }

    private <T extends Enum<T> & ErrorCodeType>  List<ErrorCodeResponse> toList(T[] codes) {
        List<ErrorCodeResponse> list = new ArrayList<>();
        for (T code : codes) {
            list.add(new ErrorCodeResponse(
                    code.name(),
                    code.getCode(),
                    getDescription(code)
            ));
        }
        return list;
    }

    private String getDescription(Enum<?> e) {

        if (e instanceof AuthExceptionCode auth) {
            switch (auth) {

                // 000번대 - 인증(Authentication)
                case AUTHENTICATION_FAILED:
                    return "인증 실패";

                case INVALID_CREDENTIALS:
                    return "아이디 또는 비밀번호가 잘못되었습니다.";

                case ACCOUNT_LOCKED:
                    return "계정이 잠겨 있습니다.";

                case ACCOUNT_DISABLED:
                    return "계정이 비활성화 상태입니다.";

                case ACCOUNT_EXPIRED:
                    return "계정이 만료되었습니다.";

                case CREDENTIALS_EXPIRED:
                    return "비밀번호 자격 증명이 만료되었습니다.";

                case MULTI_FACTOR_REQUIRED:
                    return "2차 인증이 필요합니다.";

                // 100번대 - 권한(Authorization)
                case ACCESS_DENIED:
                    return "접근 권한이 없습니다.";

                case SESSION_EXPIRED:
                    return "세션이 만료되었습니다.";

                // 200번대 - 토큰(Token)
                case TOKEN_MISSING:
                    return "인증 토큰이 누락되었습니다.";

                case TOKEN_INVALID:
                    return "인증 토큰이 유효하지 않습니다.";

                case TOKEN_EXPIRED:
                    return "인증 토큰이 만료되었습니다.";

                default:
                    return auth.name();
            }
        }


        if (e instanceof RequestExceptionCode req) {
            switch (req) {

                // 000번대: 클라이언트 요청 자체의 문제
                case INVALID_PARAMETER:
                    return "요청 파라미터가 유효하지 않습니다.";

                case MISSING_PARAMETER:
                    return "필수 파라미터가 누락되었습니다.";

                case INVALID_FORMAT:
                    return "요청 형식이 잘못되었습니다.";

                case DUPLICATE_REQUEST:
                    return "중복된 요청입니다.";

                case UNSUPPORTED_MEDIA_TYPE:
                    return "지원하지 않는 미디어 타입입니다.";

                case METHOD_NOT_ALLOWED:
                    return "허용되지 않은 HTTP 메서드입니다.";

                case REQUEST_BODY_REQUIRED:
                    return "요청 본문(Request Body)이 필요합니다.";

                case PAYLOAD_TOO_LARGE:
                    return "요청 데이터 크기가 너무 큽니다.";

                case INVALID_PAGE_NUMBER:
                    return "유효하지 않은 페이지 번호입니다.";

                case PAGE_NOT_FOUND:
                    return "요청한 페이지를 찾을 수 없습니다.";

                // 100번대: 인증 및 인가 관련
                case UNAUTHORIZED:
                    return "인증이 필요합니다.";

                case FORBIDDEN:
                    return "접근 권한이 없습니다.";

                case TOKEN_EXPIRED:
                    return "인증 토큰이 만료되었습니다.";

                case TOKEN_INVALID:
                    return "유효하지 않은 인증 토큰입니다.";

                case ACCESS_DENIED_BY_ROLE:
                    return "해당 역할(Role)로는 접근이 거부되었습니다.";

                case SESSION_EXPIRED:
                    return "세션이 만료되었습니다.";

                case SESSION_MISMATCH:
                    return "세션이 일치하지 않습니다.";

                // 200번대: 리소스를 찾을 수 없음
                case NOT_FOUND:
                    return "요청한 리소스를 찾을 수 없습니다.";

                case RESOURCE_GONE:
                    return "요청한 리소스가 삭제되었거나 더 이상 존재하지 않습니다.";

                case EMPTY_RESULT:
                    return "요청 결과가 비어 있습니다.";

                case ID_MISMATCH:
                    return "경로 변수와 요청 본문의 ID가 일치하지 않습니다.";

                default:
                    return req.name();
            }
        }


        if (e instanceof ServerExceptionCode srv) {
            switch (srv) {

                // 000번대: 일반 서버 오류
                case INTERNAL_SERVER_ERROR:
                    return "서버 내부 오류";

                case SERVICE_UNAVAILABLE:
                    return "서비스가 일시 중단되었거나 준비되지 않았습니다.";

                case UNKNOWN_EXCEPTION:
                    return "정의되지 않은 서버 예외가 발생했습니다.";

                case CONFIGURATION_ERROR:
                    return "서버 설정 오류가 발생했습니다.";

                // 100번대: DB 관련 예외
                case DATABASE_ERROR:
                    return "데이터베이스 오류가 발생했습니다.";

                case DUPLICATE_KEY:
                    return "중복된 키 값이 존재합니다.";

                case CONSTRAINT_VIOLATION:
                    return "DB 제약 조건 위반입니다.";

                case DATA_INTEGRITY_ERROR:
                    return "데이터 무결성 오류가 발생했습니다.";

                case QUERY_TIMEOUT:
                    return "쿼리 실행 시간이 초과되었습니다.";

                case DEADLOCK_DETECTED:
                    return "데이터베이스 교착 상태(Deadlock)가 발생했습니다.";

                case INTERRUPTED:
                    return "서버 작업이 중단되었습니다.";

                // 200번대: 파일/IO/서버 리소스
                case FILE_IO_ERROR:
                    return "파일 입출력 오류가 발생했습니다.";

                case FILE_NOT_FOUND:
                    return "파일을 찾을 수 없습니다.";

                case STORAGE_FULL:
                    return "저장 공간이 부족합니다.";

                case MEMORY_OVERLOAD:
                    return "메모리 과부하가 발생했습니다.";

                default:
                    return srv.name();
            }
        }


        if (e instanceof UserExceptionCode user) {
            switch (user) {
                case USER_NOT_FOUND:
                    return "사용자를 찾을 수 없습니다.";

                case DUPLICATE_USER:
                    return "이미 존재하는 사용자";

                case USER_ALREADY_DELETED:
                    return "이미 탈퇴한 사용자입니다.";

                case USER_INACTIVE:
                    return "비활성화된 사용자입니다.";

                case ACCOUNT_LOCKED:
                    return "계정이 잠겨 있습니다.";

                case ACCOUNT_DISABLED:
                    return "계정이 비활성화되어 있습니다.";

                case ACCOUNT_SUSPENDED:
                    return "계정이 일시 정지되었습니다.";

                case ACCOUNT_BANNED:
                    return "계정이 영구 정지되었습니다.";

                default:
                    return user.name();
            }
        }

        return e.name(); // 아무 것도 매칭되지 않을 때
    }
}
