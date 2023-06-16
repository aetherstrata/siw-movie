package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>
{
    List<Review> findAllByMovie(Movie movie);
    Page<Review> findAllByMovie(Movie movie, Pageable pageable);

    long countByMovie(Movie movie);
}
