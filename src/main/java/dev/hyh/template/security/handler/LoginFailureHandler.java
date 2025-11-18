package dev.hyh.template.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hyh.template.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.*;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        log.warn("LOGIN FAILED: {}", exception.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // ApiResponse 기반 실패 JSON
        ApiResponse<Void> errorResponse =
                ApiResponse.error(
                        "아이디 또는 비밀번호가 일치하지 않습니다.",
                        "AUTH001"
                );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
