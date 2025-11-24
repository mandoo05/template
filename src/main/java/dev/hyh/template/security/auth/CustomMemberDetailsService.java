package dev.hyh.template.security.auth;

import dev.hyh.template.domain.member.infra.entity.Member;
import dev.hyh.template.domain.member.infra.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Member member = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        return CustomMemberDetails.builder()
                .id(String.valueOf(member.getId()))
                .username(member.getUsername())
                .name(member.getName())
                .password(member.getPassword())
                .authorities(Collections.singleton(
                        new SimpleGrantedAuthority(member.getRole().name())
                ))
                .build();
    }
}
