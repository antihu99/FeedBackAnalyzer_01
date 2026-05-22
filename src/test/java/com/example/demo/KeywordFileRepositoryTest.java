package com.example.demo;

import com.example.demo.config.Sentiment;
import com.example.demo.model.KeywordStore;
import com.example.demo.repository.KeywordFileRepository;
import com.example.demo.service.KeywordConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeywordFileRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("TC-KEYWORD-01: save/load round trip")
    void saveAndLoad_roundTrip() throws Exception {
        Path file = tempDir.resolve("keywords.json");
        KeywordFileRepository repository = new KeywordFileRepository();
        KeywordStore store = KeywordConfigService.createSeedFromConstants();
        store.getSentiment().get(Sentiment.POSITIVE.getLabel()).add("테스트키워드");

        repository.save(file, store);
        KeywordStore loaded = repository.load(file);

        List<String> positive = loaded.getSentiment().get(Sentiment.POSITIVE.getLabel());
        assertTrue(positive.contains("테스트키워드"));
        assertEquals(store.getCategory().keySet(), loaded.getCategory().keySet());
    }
}
