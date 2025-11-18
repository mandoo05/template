package dev.hyh.template.security.auth;

import dev.hyh.template.domain.member.infra.User;
import dev.hyh.template.domain.member.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        return CustomUserDetails.builder()
                .userId(String.valueOf(user.getId()))
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singleton(
                        new SimpleGrantedAuthority(user.getRole().name())
                ))
                .build();
    }
}
