package com.example.demo.service;

import com.example.demo.config.Constants;
import com.example.demo.config.Sentiment;
import com.example.demo.model.KeywordStore;
import com.example.demo.repository.KeywordFileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordConfigService {

    private final KeywordFileRepository repository;
    private final Path storePath;
    private KeywordStore store;

    public KeywordConfigService(
            KeywordFileRepository repository,
            @Value("${keywords.file.path:data/keywords.json}") String path) {
        this.repository = repository;
        this.storePath = path == null ? null : Paths.get(path);
    }

    /** 단위 테스트·기존 TC용 in-memory 시드 (파일 미사용) */
    public static KeywordConfigService createInMemoryFromConstants() {
        KeywordConfigService service = new KeywordConfigService(new KeywordFileRepository(), null);
        service.store = createSeedFromConstants();
        return service;
    }

    /** @TempDir 등 파일 경로 지정 테스트 */
    public static KeywordConfigService forPath(Path path) {
        KeywordConfigService service = new KeywordConfigService(new KeywordFileRepository(), path.toString());
        service.loadOrSeed();
        return service;
    }

    @PostConstruct
    public void init() {
        if (storePath != null) {
            loadOrSeed();
        } else if (store == null) {
            store = createSeedFromConstants();
        }
    }

    public void loadOrSeed() {
        try {
            KeywordStore loaded = repository.load(storePath);
            if (loaded != null && loaded.getSentiment() != null && !loaded.getSentiment().isEmpty()) {
                store = loaded;
            } else {
                store = createSeedFromConstants();
                persist();
            }
        } catch (IOException e) {
            store = createSeedFromConstants();
        }
    }

    public void reload() {
        loadOrSeed();
    }

    public Map<String, List<String>> getSentimentKeywords() {
        return store.getSentiment();
    }

    public Map<String, Map<String, Object>> getCategoryKeywords() {
        return store.getCategory();
    }

    public List<String> getCategoryNames() {
        return new ArrayList<>(store.getCategory().keySet());
    }

    public void addSentimentKeyword(String sentimentLabel, String keyword) {
        store.getSentiment().computeIfAbsent(sentimentLabel, k -> new ArrayList<>());
        List<String> list = store.getSentiment().get(sentimentLabel);
        if (!list.contains(keyword)) {
            list.add(keyword);
        }
        persist();
    }

    public void removeSentimentKeyword(String sentimentLabel, String keyword) {
        List<String> list = store.getSentiment().get(sentimentLabel);
        if (list != null) {
            list.remove(keyword);
        }
        persist();
    }

    private void persist() {
        if (storePath == null) {
            return;
        }
        try {
            repository.save(storePath, store);
        } catch (IOException ignored) {
            // runtime: log in production
        }
    }

    public static KeywordStore createSeedFromConstants() {
        KeywordStore seed = new KeywordStore();
        Map<String, List<String>> sentiment = new LinkedHashMap<>();
        Constants.SENTIMENT_KEYWORDS.forEach((k, v) -> sentiment.put(k, new ArrayList<>(v)));
        seed.setSentiment(sentiment);

        Map<String, Map<String, Object>> category = new LinkedHashMap<>();
        Constants.CATEGORY_KEYWORDS.forEach((name, value) -> {
            Map<String, Object> copy = new HashMap<>();
            copy.put("main", new ArrayList<>((List<String>) value.get("main")));
            @SuppressWarnings("unchecked")
            Map<String, List<String>> sub = (Map<String, List<String>>) value.get("sub");
            Map<String, List<String>> subCopy = new LinkedHashMap<>();
            if (sub != null) {
                sub.forEach((sk, sv) -> subCopy.put(sk, new ArrayList<>(sv)));
            }
            copy.put("sub", subCopy);
            category.put(name, copy);
        });
        seed.setCategory(category);
        return seed;
    }
}
