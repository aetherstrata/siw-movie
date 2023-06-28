package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long>
{
    List<Artist> findByDirectedMoviesNotEmpty();
    Page<Artist> findByDirectedMoviesNotEmpty(Pageable pageable);

    List<Artist> findByStarredMoviesNotEmpty();
    Page<Artist> findByStarredMoviesNotEmpty(Pageable pageable);

    List<Artist> findByStarredMoviesContains(Movie movie);
    Page<Artist> findByStarredMoviesContains(Movie movie, Pageable pageable);

    List<Artist> findByStarredMoviesNotContains(Movie movie);
    Page<Artist> findByStarredMoviesNotContains(Movie movie, Pageable pageable);
}
