package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long>
{
    List<Artist> findByDirectedMoviesContains(Movie movie);
    List<Artist> findByDirectedMoviesContains(Movie movie, Pageable pageable);

    List<Artist> findByStarredMoviesContains(Movie movie);
    List<Artist> findByStarredMoviesContains(Movie movie, Pageable pageable);
}
