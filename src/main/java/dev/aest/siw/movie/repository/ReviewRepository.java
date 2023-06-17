package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>
{
    List<Review> findAllByMovie(Movie movie);
    Page<Review> findAllByMovie(Movie movie, Pageable pageable);

    Optional<Review> findByMovieAndUser(Movie movie, User user);

    boolean existsByMovieAndUser(Movie movie, User user);

    long countByMovie(Movie movie);
}
