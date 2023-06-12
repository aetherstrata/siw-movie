package dev.aest.siw.movie.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class FileService implements IFileService
{
    protected final Path root;

    protected FileService(Path root) {
        this.root = root;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload! => " + root);
        }
    }

    @Override
    public Path save(MultipartFile file) {
        Path filepath = this.root.resolve(UUID.randomUUID() + "-" + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), filepath);
            return root.relativize(filepath);
        } catch (FileAlreadyExistsException e) {
            throw new RuntimeException("A file of that name already exists. => " + filepath);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.isReadable())
                return resource;

            throw new RuntimeException("Could not read file! => " + file.toAbsolutePath());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try (var files = Files.walk(this.root, 1)){
            return files.filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files in " + root);
        }
    }
}
