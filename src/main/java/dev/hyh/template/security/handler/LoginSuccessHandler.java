package dev.hyh.template.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.security.auth.CustomUserDetails;
import dev.hyh.template.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> claims = Map.of(
                "username", user.getUsername(),
                "role", user.getAuthorities().iterator().next().getAuthority()
        );

        String accessToken = jwtProvider.createAccessToken(user.getUserId(), claims);

        Map<String, Object> payload = new HashMap<>();
        payload.put("accessToken", "Bearer " + accessToken);

        ApiResponse<Object> apiResponse = ApiResponse.success(payload);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        log.info("LOGIN SUCCESS → {}", user.getUsername());
    }
}
