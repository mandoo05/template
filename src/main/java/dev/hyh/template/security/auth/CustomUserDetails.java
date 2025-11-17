package dev.hyh.template.security.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private final String userId;   // PK: UUID or Long → String 통일
    private final String username; // 로그인 ID
    private final String password; // 로그인 시에만 사용
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 관리할 필요 없으므로 기본 true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 관리 X → 기본 true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 정책 없으면 true
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성/비활성 기능 없으면 항상 true
    }
}
