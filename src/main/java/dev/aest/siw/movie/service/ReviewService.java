package dev.aest.siw.movie.service;

import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.entity.Review;
import dev.aest.siw.movie.entity.User;
import dev.aest.siw.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService
{
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Review getReview(Long id) {
        return this.reviewRepository.findById(id).orElse(null);
    }

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

    @Transactional(readOnly = true)
    public List<Review> getReviewsForMovie(Long id) {
        return this.reviewRepository.findAllByMovieId(id);
    }

    @Transactional(readOnly = true)
    public boolean userHasReview(Movie movie, User user){
        return this.reviewRepository.existsByMovieAndUser(movie, user);
    }

    @Transactional(readOnly = true)
    public Review getUserReview(Movie movie, User user){
        return this.reviewRepository.findByMovieAndUser(movie, user).orElse(null);
    }

    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Review review) {
        this.reviewRepository.delete(review);
    }
}
