package dev.hyh.template.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hyh.template.common.response.ApiResponse;
import dev.hyh.template.security.auth.CustomMemberDetails;
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

        CustomMemberDetails user = (CustomMemberDetails) authentication.getPrincipal();

        Map<String, Object> claims = Map.of(
                "username", user.getUsername(),
                "role", user.getAuthorities().iterator().next().getAuthority()
        );

        String accessToken = jwtProvider.createAccessToken(user.getUsername(), claims);
        String refreshToken = jwtProvider.createRefreshToken(user.getUsername());

        refreshTokenRepository.save(
                user.getUsername(),
                refreshToken,
                jwtProvider.getRefreshTokenExpireMs()
        );

        ApiResponse<String> result = ApiResponse.success("Bearer " + accessToken);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));

        log.info("LOGIN SUCCESS → {}", user.getUsername());
    }
}
