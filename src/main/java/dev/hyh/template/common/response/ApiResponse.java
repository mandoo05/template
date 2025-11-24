package dev.hyh.template.common.response;


import dev.hyh.template.common.exception.CustomRuntimeException;
import lombok.*;

@Getter
@Builder
public class ApiResponse<T> {
    private static final Integer SUCCESS = 200;

    private String message;
    private Integer code;
    private T data;

    public String getStatus() {
        return (this.code != null && this.code.equals(SUCCESS)) ? "success" : "error";
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .message("")
                .code(SUCCESS)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .message(message)
                .code(SUCCESS)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> noContentSuccess() {
        return ApiResponse.<Void>builder()
                .message("")
                .code(SUCCESS)
                .data(null)
                .build();
    }

    public static ApiResponse<Void> noContentSuccess(String message) {
        return ApiResponse.<Void>builder()
                .message(message)
                .code(SUCCESS)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Integer code, T data) {
        return ApiResponse.<T>builder()
                .message(message)
                .code(code)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> error(String message, Integer code) {
        return ApiResponse.<Void>builder()
                .message(message)
                .code(code)
                .data(null)
                .build();
    }

    public static ApiResponse<Void> error(CustomRuntimeException e) {
        return ApiResponse.<Void>builder()
                .message(e.getMessage())
                .code(e.getErrorCode().getCode())
                .data(null)
                .build();
    }
}
