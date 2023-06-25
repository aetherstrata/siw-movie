package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long>
{
    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.starredMovies LEFT JOIN FETCH a.directedMovies WHERE a.id=:id")
    Optional<Artist> findFullById(Long id);

    List<Artist> findByDirectedMoviesNotEmpty();
    Page<Artist> findByDirectedMoviesNotEmpty(Pageable pageable);

    List<Artist> findByStarredMoviesNotEmpty();
    Page<Artist> findByStarredMoviesNotEmpty(Pageable pageable);

    List<Artist> findByDirectedMoviesContains(Movie movie);
    Page<Artist> findByDirectedMoviesContains(Movie movie, Pageable pageable);

    List<Artist> findByStarredMoviesContains(Movie movie);
    Page<Artist> findByStarredMoviesContains(Movie movie, Pageable pageable);

    List<Artist> findByStarredMoviesNotContains(Movie movie);
    Page<Artist> findByStarredMoviesNotContains(Movie movie, Pageable pageable);
}
