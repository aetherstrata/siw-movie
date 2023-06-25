package dev.aest.siw.movie.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IFileService
{
    void init();

    Path save(MultipartFile file);

    Resource load(String filename);
}
