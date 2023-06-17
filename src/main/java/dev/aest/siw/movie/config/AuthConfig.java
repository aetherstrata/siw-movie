package dev.aest.siw.movie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

import static dev.aest.siw.movie.model.Credentials.ADMIN_AUTHORITY;

@Configuration
@EnableWebSecurity
public class AuthConfig
{
    private final DataSource dataSource;

    public AuthConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 AS enabled FROM credentials WHERE username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configureHttpSecurity(final HttpSecurity httpSecurity) throws Exception {
        // See https://stackoverflow.com/questions/75222930/spring-boot-3-0-2-adds-continue-query-parameter-to-request-url-after-login
        // HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        // requestCache.setMatchingRequestParameterName(null);

        httpSecurity
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().cors().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/","/register", "/movies", "/movies/{id}", "/search/movies", "/artists", "/artists/{id}", "/search/artists", "/css/**", "/images/**", "/uploads/**", "/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/login", "/movies/search").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(ADMIN_AUTHORITY)
                        .anyRequest().authenticated()
                )
                //.requestCache(cache -> cache
                //        .requestCache(requestCache))
                .formLogin(authorize -> authorize
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/success")
                        .permitAll())
                .oauth2Login(authorize -> authorize
                        .loginPage("/login"))
                .logout(authorize -> authorize
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                        .permitAll());
        return httpSecurity.build();
    }
}
