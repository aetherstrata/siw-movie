package dev.aest.siw.movie.service;

import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MovieFileService extends FileService
{
    private static final Path MOVIE_ROOT = Paths.get("./uploads/movies/images");

    protected MovieFileService() {
        super(MOVIE_ROOT);
    }
}
