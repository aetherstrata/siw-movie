package dev.aest.siw.movie.auth;

import dev.aest.siw.movie.entity.User;
import dev.aest.siw.movie.repository.UserRepository;
import dev.aest.siw.movie.service.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static dev.aest.siw.movie.entity.Credentials.ADMIN_AUTHORITY;
import static dev.aest.siw.movie.entity.Credentials.DEFAULT_AUTHORITY;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig
{
    private final PasswordEncoder passwordEncoder;
    private final CredentialsService credentialsService;
    private final UserRepository userRepository;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(credentialsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    protected SecurityFilterChain configureHttpSecurity(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .cors().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/","/register", "/movies", "/movies/{id}", "/movies/{id}/images", "/artists", "/artists/{id}", "/api/v1/**","/css/**", "/images/**", "/uploads/**", "/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(ADMIN_AUTHORITY)
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/success", true)
                        .permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/oauth2-success", true)
                        .userInfoEndpoint().userService(oauth2UserService()))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                        .permitAll());
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return request -> {
            OAuth2User oauth2User = delegate.loadUser(request);

            Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(DEFAULT_AUTHORITY));

            // Extract relevant information from the OAuth2User object
            String email = oauth2User.getAttribute("email");
            String username = oauth2User.getAttribute("login");
            String name =  oauth2User.getAttribute("name");
            String provider = request.getClientRegistration().getRegistrationId();

            // Check if the user already exists in the database
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                return new OAuth2Credentials(user, provider, username, authorities);
            }

            // Register a new user
            User newUser = new User();
            newUser.setNickname(username);
            newUser.setEmail(email);
            newUser.setName(name);
            userRepository.save(newUser);
            return new OAuth2Credentials(newUser, provider, username, authorities);

            //Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());
        };
    }
}
