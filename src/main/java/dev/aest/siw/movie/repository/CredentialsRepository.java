package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Credentials;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialsRepository extends CrudRepository<Credentials, UUID>
{
    Optional<Credentials> findByUsername(String username);
}
