package dev.aest.siw.movie;

import dev.aest.siw.movie.service.ArtistFileService;
import dev.aest.siw.movie.service.MovieFileService;

import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiwMovie implements CommandLineRunner
{
	public static final String PROJECT_NAME = "SIW Movie";

	@Resource
	private ArtistFileService artistFileService;

	@Resource
	private MovieFileService movieFileService;

	public static void main(String[] args) {
		SpringApplication.run(SiwMovie.class, args);
	}

	@Override
	public void run(String... args) {
		artistFileService.init();
		movieFileService.init();
	}
}
