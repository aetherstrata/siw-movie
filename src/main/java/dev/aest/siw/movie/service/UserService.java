package dev.aest.siw.movie.service;

import dev.aest.siw.movie.auth.OAuth2Credentials;
import dev.aest.siw.movie.entity.Credentials;
import dev.aest.siw.movie.entity.User;
import dev.aest.siw.movie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService
{
    /**
     * Retrieve the current {@link User} from the database.
     * @return the retrieved {@link User}, or null if no {@link User} is logged in
     */
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Credentials local) return local.getUser();
        else if (principal instanceof OAuth2Credentials oauth) return oauth.getUser();
        else return null;
    }
}
