package dev.aest.siw.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Credentials implements UserDetails
{
    public static final String DEFAULT_AUTHORITY = "REGISTERED";
    public static final String ADMIN_AUTHORITY = "ADMIN";

    @Id
    @GeneratedValue
    private UUID id;

    @NaturalId
    @Column(nullable = false, unique = true)
    @NotBlank(message = "{username.blank}")
    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "{username.invalid}")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "{password.blank}")
    @Pattern(regexp = "^(?=(.*[A-Z])+)(?=(.*[a-z])+)(?=(.*[0-9])+)(?=(.*[!@#$%^&*()_+=.])+).{10,}$", message = "{password.weak}")
    private String password;

    private String authority;

    @Column(nullable = false)
    private Boolean enabled = true;

    @OneToOne(cascade = CascadeType.ALL)
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
        if (!(o instanceof Credentials other)) return false;

        return username.equals(other.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
