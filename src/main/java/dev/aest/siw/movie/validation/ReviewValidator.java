package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.entity.Review;

import dev.aest.siw.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ReviewValidator implements Validator
{
    private final ReviewService reviewService;

    @Override
    public boolean supports(Class<?> type) {
        return Review.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Review reviewFormData = (Review) target;
        if (reviewService.userHasReview(reviewFormData.getMovie(), reviewFormData.getUser())){
            errors.reject("review.duplicate", "Another review from this user already exists for this movie.");
        }
    }
}
