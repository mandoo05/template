package dev.hyh.template.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.security.JwtResponse;
import dev.hyh.template.security.auth.CustomUserDetails;
import dev.hyh.template.security.jwt.JwtProvider;
import dev.hyh.template.security.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
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
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        // 🔥 Redis에 RefreshToken 저장
        refreshTokenRepository.save(
                user.getUserId(),
                refreshToken,
                jwtProvider.getRefreshTokenExpireMs()
        );

        // 🔥 API 응답
        ApiResponse<?> result = ApiResponse.success(
                JwtResponse.from(accessToken)
        );

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));

        log.info("LOGIN SUCCESS → {}", user.getUsername());
    }
}
