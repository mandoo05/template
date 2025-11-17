package dev.hyh.template.common.response;


import dev.hyh.template.common.exception.custom.CustomRuntimeException;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private static final String SUCCESS = "SUCCESS";

    private String message;
    private String code;
    private T payload;

    public static <T> ApiResponse<T> success(T payload) {
        return ApiResponse.<T>builder().message("").code(SUCCESS).payload(payload).build();
    }

    public static <T> ApiResponse<T> success(String message, T payload) {
        return ApiResponse.<T>builder().message(message).code(SUCCESS).payload(payload).build();
    }

    public static ApiResponse<Void> noContentSuccess() {
        return ApiResponse.<Void>builder().message("").code(SUCCESS).payload(null).build();
    }

    public static ApiResponse<Void> noContentSuccess(String message) {
        return ApiResponse.<Void>builder().message(message).code(SUCCESS).payload(null).build();
    }

    public static <T> ApiResponse<T> error(String message, String code, T payload) {
        return ApiResponse.<T>builder().message(message).code(code).payload(payload).build();
    }

    public static ApiResponse<Void> error(String message, String code) {
        return ApiResponse.<Void>builder().message(message).code(code).payload(null).build();
    }

    public static ApiResponse<Void> error(CustomRuntimeException e) {
        return ApiResponse.<Void>builder()
                .message(e.getMessage())
                .code(e.getCode())
                .payload(null)
                .build();
    }
}
