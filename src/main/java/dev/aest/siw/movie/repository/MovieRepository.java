package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>
{
    List<Movie> findAllByDirector(Artist artist);
    List<Movie> findAllByActorsContaining(Artist artist);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.actors WHERE m.id=:id")
    Optional<Movie> findDetailedById(long id);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.imageUrls WHERE m.id=:id")
    Optional<Movie> findWithImagesById(long id);

    boolean existsByTitleAndYear(String title, int year);
}
