package dev.hyh.template.domain.member.service;

import dev.hyh.template.common.exception.custom.CustomRuntimeException;
import dev.hyh.template.domain.member.Role;
import dev.hyh.template.domain.member.dto.TestResponse;
import dev.hyh.template.domain.member.dto.UserAuthRequest;
import dev.hyh.template.domain.member.infra.User;
import dev.hyh.template.domain.member.infra.UserRepository;
import dev.hyh.template.security.auth.CustomUserDetails;
import dev.hyh.template.security.auth.CustomUserDetailsService;
import dev.hyh.template.security.jwt.JwtProvider;
import dev.hyh.template.security.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(UserAuthRequest.UserPostRequest dto) {
        User user = User.builder()
                .username(dto.username())
                .name(dto.name())
                .role(Role.ROLE_USER)
                .build();

        user.updatePassword(dto.password(), passwordEncoder);

        userRepository.save(user);
    }

    public TestResponse.testGetResponse get(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomRuntimeException("user not found"));
        return TestResponse.testGetResponse.from(user);
    }

    public String reissueAccessToken(String refreshTokenHeader) {

        String refreshToken = refreshTokenHeader.replace("Bearer ", "");

        // 1️⃣ refreshToken 유효성 검사
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("REFRESH_TOKEN_INVALID");
        }

        // 2️⃣ claim에서 userId 추출
        String userId = jwtProvider.getRefreshClaims(refreshToken).getSubject();

        // 3️⃣ Redis에 저장된 refreshToken 확인
        String storedToken = refreshTokenRepository.find(userId);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException("REFRESH_TOKEN_NOT_FOUND_OR_MISMATCH");
        }

        // 4️⃣ 새 AccessToken 발급
        return jwtProvider.recreateAccessToken(userId);
    }


}
