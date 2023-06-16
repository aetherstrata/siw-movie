package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.model.User;
import dev.aest.siw.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService
{
    private static final int MAX_PAGE_SIZE = 50;

    private final ReviewRepository reviewRepository;

    /**
     * Get a {@link Page} of {@link Review Reviews} of the given size
     * @param movie the movie to retrieve the reviews for
     * @param page the page index
     * @param size the page size
     * @return the corresponding {@link Page}. It will be {@code empty} if no item satisfies the query.
     */
    @Transactional(readOnly = true)
    public Page<Review> getReviewPage(Movie movie, int page, int size){
        Pageable paging = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE));
        return reviewRepository.findAllByMovie(movie, paging);
    }

    /**
     * Get a {@link Page} of {@link Review Reviews}<br>
     * See {@link ReviewService#getReviewPage(Movie, int, int)} for more info
     * @return a maximum size {@link Page} of {@link Review Reviews}
     */
    @Transactional(readOnly = true)
    public Page<Review> getReviewPage(Movie movie, int page){
        return getReviewPage(movie, page, MAX_PAGE_SIZE);
    }

    /**
     * Get the count of {@link Review Reviews} for a specific {@link Movie}
     * @param movie the movie to query against
     * @return the count of related {@link Review Reviews}
     */
    @Transactional(readOnly = true)
    public long getCountByMovie(Movie movie){
        return reviewRepository.countByMovie(movie);
    }

    /**
     * Get the review of a {@link User} for a {@link Movie}
     * @param user the user to query against
     * @param movie the movie to query against
     * @return An {@link Optional} {@link Review}
     */
    @Transactional(readOnly = true)
    public Optional<Review> getUserReview(User user, Movie movie) {
        return reviewRepository.findByMovieAndUser(movie, user);
    }
}
