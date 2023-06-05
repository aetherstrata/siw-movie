package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MovieService
{
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieve a {@link Movie} from the database based on its ID.
     * @param id the id of the {@link Movie} to retrieve from the database
     * @return the retrieved {@link Movie}, or null if no {@link Movie} with the passed ID could be found in the database
     */
    @Transactional
    public Movie getMovie(Long id) {
        Optional<Movie> result = this.movieRepository.findById(id);
        return result.orElse(null);
    }
}
