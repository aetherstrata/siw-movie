package dev.aest.siw.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public final class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NaturalId
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email must not be whitespace")
    @Email
    private String email;

    private String nickname;

    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    public User(){
        reviews = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;

        return email.equals(other.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
