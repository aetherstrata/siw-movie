package dev.aest.siw.movie.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MovieFileService extends FileService
{
    private static final Path MOVIE_ROOT = Paths.get("./uploads/movies/images");

    protected MovieFileService() {
        super(MOVIE_ROOT);
    }
}
