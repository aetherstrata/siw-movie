package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService
{
    private static final int MAX_PAGE_SIZE = 50;

    private final MovieRepository movieRepository;

    /**
     * Retrieve a {@link Movie} from the database based on its ID.
     * @param id the id of the {@link Movie} to retrieve from the database
     * @return the retrieved {@link Movie}, or null if no {@link Movie} with the passed ID could be found in the database
     */
    @Transactional(readOnly = true)
    public Movie getMovie(Long id) {
        Optional<Movie> result = this.movieRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviePage(int page, int size){
        Pageable paging = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE));
        return movieRepository.findAll(paging);
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviePage(int page){
        return getMoviePage(page, MAX_PAGE_SIZE);
    }

    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }
}
