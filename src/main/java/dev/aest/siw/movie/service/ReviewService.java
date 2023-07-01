package dev.aest.siw.movie.service;

import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.entity.Review;
import dev.aest.siw.movie.entity.User;
import dev.aest.siw.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService
{
    private static final int MAX_PAGE_SIZE = 50;

    private final ReviewRepository reviewRepository;

    /**
     * Get a {@link Page} of {@link Review Reviews} with the given {@link Pageable}
     * @param movie the movie to retrieve the reviews for
     * @param pageable the paging information
     * @return the corresponding {@link Page}. It will be {@code empty} if no item satisfies the query.
     */
    @Transactional(readOnly = true)
    public Page<Review> getReviewPage(Movie movie, Pageable pageable){
        return reviewRepository.findAllByMovie(movie, pageable);
    }

    /**
     * Get a {@link Page} of {@link Review Reviews} of the given size
     * @param movie the movie to retrieve the reviews for
     * @param page the page index
     * @param size the page size
     * @return the corresponding {@link Page}. It will be {@code empty} if no item satisfies the query.
     */
    @Transactional(readOnly = true)
    public Page<Review> getReviewPage(Movie movie, int page, int size){
        return getReviewPage(movie, PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE)));
    }

    @Transactional(readOnly = true)
    public boolean userHasReview(Movie movie, User user){
        return this.reviewRepository.existsByMovieAndUser(movie, user);
    }

    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsForMovie(Long id) {
        return this.reviewRepository.findAllByMovieId(id);
    }
}
