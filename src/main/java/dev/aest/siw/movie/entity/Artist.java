package dev.aest.siw.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "artists")
public final class Artist
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

	@Column(nullable = false)
	@NotBlank(message = "{artist.name.blank}")
	private String name;

	@Column(nullable = false)
	@NotBlank(message = "{artist.surname.blank}")
	private String surname;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfDeath;

	private String imageUrl;
	
	@ManyToMany(mappedBy="actors")
	private Set<Movie> starredMovies;
	
	@OneToMany(mappedBy="director")
	private List<Movie> directedMovies;
	
	public Artist(){
		this.starredMovies = new HashSet<>();
		this.directedMovies = new LinkedList<>();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Artist other)) return false;

		if (!name.equals(other.name)) return false;
		return  surname.equals(other.surname);
	}

}