package dev.aest.siw.movie.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService
{
    void init();

    String save(MultipartFile file);

    Resource load(String filename);

    boolean delete(String filename);
}
