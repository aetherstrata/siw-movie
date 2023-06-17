package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ReviewValidator implements Validator
{
    private final ReviewRepository reviewRepository;

    @Override
    public boolean supports(Class<?> type) {
        return Review.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Review review = (Review)target;
        if (reviewRepository.existsByMovieAndUser(review.getMovie(), review.getUser())){
            errors.reject("review.duplicate", "Another review from this user already exists for this movie.");
        }
    }
}
