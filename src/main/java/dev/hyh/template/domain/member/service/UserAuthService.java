package dev.hyh.template.domain.member.service;

import dev.hyh.template.domain.member.Role;
import dev.hyh.template.domain.member.dto.UserAuthRequest;
import dev.hyh.template.domain.member.infra.User;
import dev.hyh.template.domain.member.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

}
