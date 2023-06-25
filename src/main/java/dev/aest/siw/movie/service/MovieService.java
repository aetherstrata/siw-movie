package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

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
        return this.movieRepository.findById(id).orElse(null);
    }

    /**
     * Retrieve a {@link Movie} from the database based on its ID and initialize the lazy associations.
     * @param id the id of the {@link Movie} to retrieve from the database
     * @return the retrieved {@link Movie} with initialized lazy associations, or null if no {@link Movie} with the passed ID could be found in the database
     */
    @Transactional(readOnly = true)
    public Movie getFullMovie(Long id){
        return this.movieRepository.findFullMovieById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Movie getDetailedMovie(Long id){
        return this.movieRepository.findDetailedById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Movie getMovieWithImages(Long id){
        return this.movieRepository.findWithImagesById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviesPage(Pageable pageable){
        return movieRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviesPage(int page, int size){
        return getMoviesPage(PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE)));
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviesPage(int page){
        return getMoviesPage(page, MAX_PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    public List<Movie> getAllFullMovies(){
        return this.movieRepository.findAllFull();
    }

    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public List<Movie> getDirectedMoviesByArtistId(Long id) {
        return this.movieRepository.findAllByDirectorId(id);
    }

    @Transactional(readOnly = true)
    public List<Movie> getStarredMoviesByArtistId(Long id) {
        return this.movieRepository.findAllByActorId(id);
    }
}
