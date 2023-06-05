package dev.aest.siw.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiwMovie
{
	public static final String PROJECT_NAME = "SIW Movie";

	public static void main(String[] args) {
		SpringApplication.run(SiwMovie.class, args);
	}

}
