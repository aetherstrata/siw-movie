package dev.aest.siw.movie.auth;

import dev.aest.siw.movie.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Data
@RequiredArgsConstructor
public final class OAuth2Credentials implements OAuth2User
{
    private final User user;
    private final String provider;
    private final String username;
    private final Set<GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return username;
    }
}
