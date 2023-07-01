package dev.aest.siw.movie.service;

import dev.aest.siw.movie.entity.Credentials;
import dev.aest.siw.movie.entity.User;
import dev.aest.siw.movie.model.RegistrationFormData;
import dev.aest.siw.movie.repository.CredentialsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CredentialsService implements UserDetailsService
{
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialsRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public Credentials registerNewUser(RegistrationFormData formData) {
        User user = new User();
        user.setName(formData.getName());
        user.setNickname(formData.getUsername());
        user.setEmail(formData.getEmail());
        Credentials credentials = new Credentials();
        credentials.setUser(user);
        credentials.setUsername(formData.getUsername());
        credentials.setPassword(this.passwordEncoder.encode(formData.getPassword()));
        credentials.setAuthority(Credentials.DEFAULT_AUTHORITY);
        this.credentialsRepository.save(credentials);
        return credentials;
    }
}
