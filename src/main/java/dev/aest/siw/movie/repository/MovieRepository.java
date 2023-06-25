package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, CrudRepository<Movie, Long>
{
    List<Movie> findAllByDirectorId(long id);

    @Query(value = "SELECT m.*, (SELECT i.image FROM movie_images i WHERE i.movie_id=m.id ORDER BY i.image LIMIT 1) AS firstImage FROM movies m WHERE m.id IN (SELECT a.movie_id FROM movie_actors a WHERE a.actor_id=:id)", nativeQuery = true)
    List<Movie> findAllByActorId(long id);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.actors LEFT JOIN FETCH m.imageUrls")
    List<Movie> findAllFull();

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.actors LEFT JOIN FETCH m.imageUrls WHERE m.id=:id")
    Optional<Movie> findFullMovieById(long id);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.actors WHERE m.id=:id")
    Optional<Movie> findDetailedById(long id);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.imageUrls WHERE m.id=:id")
    Optional<Movie> findWithImagesById(long id);

    boolean existsByTitleAndYear(String title, int year);
}
