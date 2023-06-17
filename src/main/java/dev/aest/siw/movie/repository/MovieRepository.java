package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>
{
    List<Movie> findByTitle(String title);
    Page<Movie> findByTitle(String title, Pageable pageable);

    List<Movie> findByYear(int year);
    Page<Movie> findByYear(int year, Pageable pageable);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.actors")
    Optional<Movie> getFullById(long id);

    boolean existsByTitleAndYear(String title, int year);
}
