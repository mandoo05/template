package dev.hyh.template.common.exception;

import dev.hyh.template.common.exception.custom.*;
import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.common.response.ErrorCode.AuthExceptionCode;
import dev.hyh.template.common.response.ErrorCode.RequestExceptionCode;
import dev.hyh.template.common.response.ErrorCode.ServerExceptionCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.DateTimeException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HackingAttemptException.class)
    public ResponseEntity<ApiResponse<Void>> handlerHackingAttemptException(
            HackingAttemptException e) {
        log.error("[{}] HackingAttemptException: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(ForbiddenException e) {
        log.warn("[{}] ForbiddenException: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException e) {
        log.warn(
                "[{}] ConstraintViolationException: {}",
                RequestExceptionCode.INVALID_PARAMETER.getCode(),
                e.getMessage());
        return ResponseEntity.badRequest()
                .body(
                        ApiResponse.error("입력값이 유효하지 않습니다.", RequestExceptionCode.INVALID_PARAMETER.getCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoHandlerFoundException e) {
        log.warn(
                "[{}] NoHandlerFoundException: {}",
                RequestExceptionCode.PAGE_NOT_FOUND.getCode(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage(), RequestExceptionCode.PAGE_NOT_FOUND.getCode()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDatabaseException(DuplicateException e) {
        log.warn("[{}] DuplicateException: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(NotFoundDataException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundDataException(NotFoundDataException e) {
        log.warn("[{}] NotFoundDataException: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleDatabaseException(InvalidRequestException e) {
        log.warn("[{}] InvalidRequestException: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException e) {
        log.warn(
                "[{}] HttpRequestMethodNotSupportedException: {}",
                RequestExceptionCode.METHOD_NOT_ALLOWED.getCode(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(e.getMessage(), RequestExceptionCode.METHOD_NOT_ALLOWED.getCode()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(
            MissingServletRequestParameterException e) {
        log.warn(
                "[{}] MissingServletRequestParameterException: {}",
                RequestExceptionCode.MISSING_PARAMETER.getCode(),
                e.getMessage());
        String msg = e.getParameterName() + "의 값이 누락되었습니다.";
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(msg, RequestExceptionCode.MISSING_PARAMETER.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        log.warn(
                "[{}] MethodArgumentNotValidException: {}",
                RequestExceptionCode.INVALID_PARAMETER.getCode(),
                e.getMessage());
        String msg =
                e.getBindingResult().getFieldErrors().stream()
                        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                        .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(msg, RequestExceptionCode.INVALID_PARAMETER.getCode()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException e) {
        Throwable rootCause = getRootCause(e);
        String message = rootCause.getMessage().toLowerCase();

        if (message.contains("duplicate")
                || message.contains("unique constraint")
                || message.contains("constraint violation")) {
            // 중복된 데이터 에러
            log.warn(
                    "[{}] DataIntegrityViolationException (Duplicate): {}",
                    ServerExceptionCode.DUPLICATE_KEY.getCode(),
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("중복된 값이 존재합니다.", ServerExceptionCode.DUPLICATE_KEY.getCode()));
        } else if (message.contains("value too long")
                || message.contains("data too long")
                || message.contains("string data right truncation")) {
            // 데이터 크기 초과 에러
            log.warn(
                    "[{}] DataIntegrityViolationException (Data too long): {}",
                    ServerExceptionCode.CONSTRAINT_VIOLATION.getCode(),
                    e.getMessage());
            return ResponseEntity.badRequest()
                    .body(
                            ApiResponse.error(
                                    "입력 값이 너무 깁니다.", ServerExceptionCode.CONSTRAINT_VIOLATION.getCode()));
        }

        // 기타 데이터 무결성 오류
        log.warn(
                "[{}] DataIntegrityViolationException: {}",
                ServerExceptionCode.DATABASE_ERROR.getCode(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.error(
                                "서버 내부 데이터 처리 오류가 발생했습니다.", ServerExceptionCode.DATABASE_ERROR.getCode()));
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidContentType(InvalidContentTypeException e) {
        log.warn(
                "[{}] InvalidContentTypeException: {}",
                RequestExceptionCode.UNSUPPORTED_MEDIA_TYPE.getCode(),
                e.getMessage());
        return ResponseEntity.badRequest()
                .body(
                        ApiResponse.error(
                                "데이터 형식이 잘못되었습니다.", RequestExceptionCode.UNSUPPORTED_MEDIA_TYPE.getCode()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        log.warn(
                "[{}] MaxUploadSizeExceededException: {}",
                RequestExceptionCode.PAYLOAD_TOO_LARGE.getCode(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(
                        ApiResponse.error("파일 크기가 너무 큽니다.", RequestExceptionCode.PAYLOAD_TOO_LARGE.getCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn(
                "[{}] HttpMessageNotReadableException: {}",
                RequestExceptionCode.INVALID_PARAMETER.getCode(),
                e.getMessage());
        return ResponseEntity.badRequest()
                .body(
                        ApiResponse.error(
                                "요청 본문이 올바르지 않습니다.", RequestExceptionCode.INVALID_PARAMETER.getCode()));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleDateTime(DateTimeException e) {
        log.warn(
                "[{}] DateTimeException: {}",
                RequestExceptionCode.INVALID_PARAMETER.getCode(),
                e.getMessage());
        return ResponseEntity.badRequest()
                .body(
                        ApiResponse.error(
                                "날짜/시간 형식이 올바르지 않습니다.", RequestExceptionCode.INVALID_PARAMETER.getCode()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn(
                "[{}] AccessDeniedException: {}",
                AuthExceptionCode.ACCESS_DENIED.getCode(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("접근 권한이 없습니다.", AuthExceptionCode.ACCESS_DENIED.getCode()));
    }

    @ExceptionHandler(RuntimeServerException.class)
    public ResponseEntity<ApiResponse<Void>> handleServerException(RuntimeServerException e) {
        log.error("[{}] ServerException: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error(
                "[{}] Unhandled Exception: {}",
                ServerExceptionCode.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage(),
                e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.error(e.getMessage(), ServerExceptionCode.INTERNAL_SERVER_ERROR.getCode()));
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause != null && cause != throwable) {
            return getRootCause(cause);
        }
        return throwable;
    }
}
