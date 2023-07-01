package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDto
{
    private Long id;
    private String title;
    private Integer score;
    private String text;

    protected ReviewDto(Review review){
        id = review.getId();
        title = review.getTitle();
        score = review.getScore();
        text = review.getText();
    }

    public static ReviewDto of(Review review) {
        if (review == null) return null;
        return new ReviewDto(review);
    }
}
