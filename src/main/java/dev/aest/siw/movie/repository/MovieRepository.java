package dev.aest.siw.movie.repository;

import dev.aest.siw.movie.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long>
{
    public List<Movie> findByTitle(String title);
    public List<Movie> findByYear(int year);

    public boolean existsByTitleAndYear(String title, int year);
}
