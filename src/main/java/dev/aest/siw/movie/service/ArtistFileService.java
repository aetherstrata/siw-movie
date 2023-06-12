package dev.aest.siw.movie.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArtistFileService extends FileService
{
    private static final Path ARTIST_ROOT = Paths.get("./uploads/artists/images");

    protected ArtistFileService() {
        super(ARTIST_ROOT);
    }
}
