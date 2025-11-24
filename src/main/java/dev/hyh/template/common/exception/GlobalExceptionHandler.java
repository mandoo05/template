package dev.hyh.template.common.exception;

import dev.hyh.template.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("Business Exception: {} - {}", e.getErrorCode().getCode(), e.getMessage());
        return ResponseEntity.ok(
                ApiResponse.error(e.getMessage(), e.getErrorCode().getCode())
        );
    }

    /**
     * Validation 예외 처리 (@Valid 실패)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "입력값이 올바르지 않습니다.";

        log.warn("Validation Exception: {}", message);

        return ResponseEntity.ok(
                ApiResponse.error(message, 400)
        );
    }

    /**
     * 타입 변환 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        Object value = e.getValue();
        Class<?> requiredType = e.getRequiredType();

        String detail = String.format(
                "Parameter '%s' with value '%s' could not be converted to type '%s'",
                paramName,
                value,
                requiredType != null ? requiredType.getSimpleName() : "Unknown"
        );

        log.warn("Type Mismatch Exception: {}", detail);

        return ResponseEntity.ok(
                ApiResponse.error("유효하지 않은 파라미터 값입니다. " + detail, 400)
        );
    }

    /**
     * 인증 예외 처리 (Spring Security)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication Exception: {}", e.getMessage());
        return ResponseEntity.ok(
                ApiResponse.error("인증되지 않은 사용자입니다.", 401)
        );
    }

    /**
     * 권한 예외 처리 (Spring Security)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Access Denied: {}", e.getMessage());
        return ResponseEntity.ok(
                ApiResponse.error("권한이 없습니다.", 403)
        );
    }

    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal Argument Exception: {}", e.getMessage());
        return ResponseEntity.ok(
                ApiResponse.error(e.getMessage(), 400)
        );
    }

    /**
     * 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected Exception", e);
        return ResponseEntity.ok(
                ApiResponse.error("서버 내부 오류가 발생했습니다.", 500)
        );
    }

    /**
     * CustomRuntimeException 처리
     */
    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomRuntimeException(CustomRuntimeException e) {
        log.warn("CustomRuntimeException: {} - {}", e.getErrorCode().getCode(), e.getMessage());
        return ResponseEntity.ok(
                ApiResponse.error(e.getMessage(), e.getErrorCode().getCode())
        );
    }
}
