package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Credentials;
import dev.aest.siw.movie.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialsService implements UserDetailsService
{
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isOAuth(Authentication auth){
        return auth instanceof OAuth2AuthenticationToken;
    }

    public boolean isAdminUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(Credentials.ADMIN_AUTHORITY));
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(UUID id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setAuthority(Credentials.DEFAULT_AUTHORITY);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialsRepository.findByUsername(username).orElse(null);
    }
}
