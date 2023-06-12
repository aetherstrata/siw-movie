package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService
{
    private static final int MAX_PAGE_SIZE = 50;

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Get a {@link Page} of {@link Review Reviews} of the given size
     * @param movie the movie to retrieve the reviews for
     * @param page the page index
     * @param size the page size
     * @return the corresponding {@link Page}. It will be {@code empty} if no item satisfies the query.
     */
    public Page<Review> getReviewPage(Movie movie, int page, int size){
        Pageable paging = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE));
        return reviewRepository.findAllByMovie(movie, paging);
    }

    /**
     * Get a maximum size {@link Page} of {@link Review Reviews}<br>
     * See {@link ReviewService#getReviewPage(Movie, int, int)} for more info
     */
    public Page<Review> getReviewPage(Movie movie, int page){
        return getReviewPage(movie, page, MAX_PAGE_SIZE);
    }
}
