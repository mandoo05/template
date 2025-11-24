package dev.hyh.template.domain.member.service;

import dev.hyh.template.domain.member.dto.enums.Role;
import dev.hyh.template.domain.member.dto.MemberAuthRequest;
import dev.hyh.template.domain.member.infra.entity.Member;
import dev.hyh.template.domain.member.infra.repository.MemberRepository;
import dev.hyh.template.security.jwt.JwtProvider;
import dev.hyh.template.security.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final MemberRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(MemberAuthRequest.MemberPostRequest dto) {
        Member member = Member.builder()
                .username(dto.username())
                .name(dto.name())
                .role(Role.ROLE_USER)
                .build();

        member.updatePassword(dto.password(), passwordEncoder);

        userRepository.save(member);
    }

    public String reissueAccessToken(String refreshTokenHeader) {

        String refreshToken = refreshTokenHeader.replace("Bearer ", "");

        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("REFRESH_TOKEN_INVALID");
        }

        String userId = jwtProvider.getRefreshClaims(refreshToken).getSubject();
        String storedToken = refreshTokenRepository.find(userId);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException("REFRESH_TOKEN_NOT_FOUND_OR_MISMATCH");
        }

        return jwtProvider.recreateAccessToken(userId);
    }


}
