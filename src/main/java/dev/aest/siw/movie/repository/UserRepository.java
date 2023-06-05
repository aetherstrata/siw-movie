package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
}
