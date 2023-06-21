package dev.aest.siw.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"}))
public final class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    @Column(nullable = false, length = 127)
    @NotBlank
    @Size(max = 127, message = "The review title must be shorter than 128 characters")
    private String title;

    @Column(nullable = false, length = 4095)
    @Size(max = 4095, message = "The review must be shorter than 4096 characters")
    private String text;

    @PrePersist
    public void onPersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (!score.equals(review.score)) return false;
        if (!title.equals(review.title)) return false;
        return text.equals(review.text);
    }

    @Override
    public int hashCode() {
        int result = score.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }
}
