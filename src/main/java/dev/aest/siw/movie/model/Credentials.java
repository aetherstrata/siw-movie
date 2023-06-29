package dev.aest.siw.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@Entity
public class Credentials implements UserDetails
{
    public static final String DEFAULT_AUTHORITY = "REGISTERED";
    public static final String ADMIN_AUTHORITY = "ADMIN";

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    @NotBlank(message = "Username must not be whitespace")
    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "Username must contain only alphanumerical characters, dots, dashes and underscores")
    private String username;

    @NotBlank(message = "Password must not be whitespace")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String authority;

    @Column(nullable = false)
    private Boolean enabled = true;

    @OneToOne(cascade = {CascadeType.REMOVE})
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authority));
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
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
