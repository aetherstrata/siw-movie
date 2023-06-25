package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto
{
    private Long id;
    private String title;
    private Integer score;
    private String text;

    public static ReviewDto of(Review review) {
        if (review == null) return null;
        return new ReviewDto(
                review.getId(),
                review.getTitle(),
                review.getScore(),
                review.getText());
    }
}
