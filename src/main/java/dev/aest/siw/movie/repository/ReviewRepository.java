package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.entity.Review;
import dev.aest.siw.movie.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, CrudRepository<Review, Long>
{
    List<Review> findAllByMovieId(Long id);

    Page<Review> findAllByMovie(Movie movie, Pageable pageable);

    Optional<Review> findByMovieAndUser(Movie movie, User user);

    boolean existsByMovieAndUser(Movie movie, User user);

    long countByMovie(Movie movie);
}
