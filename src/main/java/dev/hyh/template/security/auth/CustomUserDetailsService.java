package dev.hyh.template.security.auth;

import dev.hyh.template.domain.member.infra.User;
import dev.hyh.template.domain.member.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 🔥 username 기준으로 DB 조회 (가장 일반적인 방식)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        // 🔥 ROLE을 문자열 → SimpleGrantedAuthority 변환
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(user.getRole().name());
        // 예: ROLE_USER

        // 🔥 UserDetails 생성
        return CustomUserDetails.builder()
                .userId(String.valueOf(user.getId()))    // Long 또는 UUID
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singleton(authority))
                .build();
    }
}
