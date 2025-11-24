package dev.hyh.template.security.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
public class CustomMemberDetails implements UserDetails {

    private final String id;
    private final String username;
    private final String name;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public boolean isAccountNonExpired() { return true; }
    public boolean isAccountNonLocked() { return true; }
    public boolean isCredentialsNonExpired() { return true; }
    public boolean isEnabled() { return true; }
}
