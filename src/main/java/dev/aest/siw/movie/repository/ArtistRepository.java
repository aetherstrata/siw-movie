package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long>
{
    List<Artist> findByDirectedMoviesNotEmpty();

    List<Artist> findByStarredMoviesNotEmpty();

    List<Artist> findByStarredMoviesContains(Movie movie);

    List<Artist> findByStarredMoviesNotContains(Movie movie);
}
