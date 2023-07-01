package dev.aest.siw.movie.service;

import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.model.MovieFormData;
import dev.aest.siw.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService
{
    private static final int MAX_PAGE_SIZE = 50;

    private final MovieRepository movieRepository;
    private final MovieFileService movieFileService;

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
        Movie movie = this.movieRepository.findById(id).orElse(null);
        if (movie == null) return null;
        Hibernate.initialize(movie.getActors());
        Hibernate.initialize(movie.getImageUrls());
        return movie;
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

    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public void updateMovie(Movie movie, MovieFormData formData){
        movie.setTitle(formData.getTitle());
        movie.setSynopsis(formData.getSynopsis());
        movie.setYear(formData.getYear());
        this.movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public List<Movie> getDirectedMoviesByArtist(Artist artist) {
        return this.movieRepository.findAllByDirector(artist);
    }

    @Transactional(readOnly = true)
    public List<Movie> getStarredMoviesByArtist(Artist artist) {
        return this.movieRepository.findAllByActorsContaining(artist);
    }

    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }

    public void addImages(Movie movie, MultipartFile... images){
        Arrays.stream(images).forEach(image -> {
            if (image != null && !image.isEmpty())
                movie.getImageUrls().add(movieFileService.save(image));
        });
    }
}
