package dev.aest.siw.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Data
public class Credentials implements UserDetails
{
    public static final String DEFAULT_ROLE = "REGISTERED";
    public static final String ADMIN_ROLE = "ADMIN";

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String role;

    @OneToOne(cascade = {CascadeType.ALL})
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
