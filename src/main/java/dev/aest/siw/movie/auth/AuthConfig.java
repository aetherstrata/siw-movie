package dev.aest.siw.movie.auth;

import dev.aest.siw.movie.model.User;
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

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static dev.aest.siw.movie.model.Credentials.ADMIN_AUTHORITY;
import static dev.aest.siw.movie.model.Credentials.DEFAULT_AUTHORITY;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig
{
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsService credentialsService;
    private final UserRepository userRepository;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(credentialsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 AS enabled FROM credentials WHERE username=?");
    }

    @Bean
    protected SecurityFilterChain configureHttpSecurity(final HttpSecurity httpSecurity) throws Exception {
        // See https://stackoverflow.com/questions/75222930/spring-boot-3-0-2-adds-continue-query-parameter-to-request-url-after-login
        // HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        // requestCache.setMatchingRequestParameterName(null);

        httpSecurity
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .cors().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/","/register", "/movies", "/movies/{id}", "/search/movies", "/artists", "/artists/{id}", "/search/artists", "/css/**", "/images/**", "/uploads/**", "/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/login", "/movies/search").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(ADMIN_AUTHORITY)
                        .anyRequest().authenticated()
                )
                //.requestCache(cache -> cache
                //        .requestCache(requestCache))
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/success")
                        .permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/oauth2-success")
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

            // Extract relevant information from the OAuth2User object
            String email = oauth2User.getAttribute("email");
            String username = oauth2User.getAttribute("login");
            String name =  oauth2User.getAttribute("name");
            String provider = request.getClientRegistration().getRegistrationId();

            Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(DEFAULT_AUTHORITY));

            // Check if the user already exists in the database
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                User user = existingUser.get();

                // Create a custom OAuth2User implementation
                return new OAuth2Credentials(user, provider, username, authorities);
            }

            // Register a new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setNickname(username);
            userRepository.save(newUser);

            // Create a custom OAuth2User implementation
            return new OAuth2Credentials(newUser, provider, username, authorities);

            //Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());
        };
    }
}
