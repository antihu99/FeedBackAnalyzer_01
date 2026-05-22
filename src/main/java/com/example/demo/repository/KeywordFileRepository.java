package com.example.demo.repository;

import com.example.demo.model.KeywordStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class KeywordFileRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KeywordStore load(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return null;
        }
        return objectMapper.readValue(path.toFile(), KeywordStore.class);
    }

    public void save(Path path, KeywordStore store) throws IOException {
        if (path == null) {
            return;
        }
        Files.createDirectories(path.getParent());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), store);
    }
}
