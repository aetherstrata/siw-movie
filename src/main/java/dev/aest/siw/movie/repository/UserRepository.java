package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    @Query(value = "SELECT * FROM users WHERE id=(SELECT user_id FROM credentials WHERE username=?1)", nativeQuery = true)
    Optional<User> findByUsername(String username);
}
