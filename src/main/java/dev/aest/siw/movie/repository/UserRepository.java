package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>
{
    Optional<User> findByEmail(String email);
}
