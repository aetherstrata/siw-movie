package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.User;
import dev.aest.siw.movie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final CredentialsService credentialsService;

    public UserService(UserRepository userRepository, CredentialsService credentialsService) {
        this.userRepository = userRepository;
        this.credentialsService = credentialsService;
    }

    /**
     * Retrieve a {@link User} from the database based on its ID.
     * @param id the id of the {@link User} to retrieve from the database
     * @return the retrieved {@link User}, or null if no {@link User} with the passed ID could be found in the database
     */
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * Retrieve the current {@link User} from the database.
     * @return the retrieved {@link User}, or null if no {@link User} is logged in
     */
    public User getCurrentUser(Principal principal) {
        Optional<User> result = this.userRepository.findByUsername(principal.getName());
        return result.orElse(null);
    }


    /**
     * Save a {@link User} in the database.
     * @param user the {@link User} to save into the database
     * @throws DataIntegrityViolationException if a {@link User} with the same username
     *                              as the passed User already exists in the database
     */
    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Retrieve all {@link User Users} from the database.
     * @return a List with all the retrieved {@link User Users}
     */
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }
}
