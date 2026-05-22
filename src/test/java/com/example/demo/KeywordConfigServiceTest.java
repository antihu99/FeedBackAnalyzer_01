package com.example.demo;

import com.example.demo.config.Sentiment;
import com.example.demo.model.Feedback;
import com.example.demo.service.KeywordConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeywordConfigServiceTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("TC-KEYWORD-02: 재기동 시뮬레이션 후 키워드 유지")
    void addKeyword_persistsAfterSimulatedRestart() {
        Path file = tempDir.resolve("keywords.json");
        KeywordConfigService first = KeywordConfigService.forPath(file);
        first.addSentimentKeyword(Sentiment.POSITIVE.getLabel(), "영속키워드");

        KeywordConfigService second = KeywordConfigService.forPath(file);
        List<String> keywords = second.getSentimentKeywords().get(Sentiment.POSITIVE.getLabel());

        assertTrue(keywords.contains("영속키워드"));
    }

    @Test
    @DisplayName("TC-KEYWORD-03: 삭제 키워드는 긍정 분류 제외")
    void removeKeyword_excludedFromClassification() {
        KeywordConfigService config = KeywordConfigService.createInMemoryFromConstants();
        config.addSentimentKeyword(Sentiment.POSITIVE.getLabel(), "유니크테스트");
        SentimentClassifier classifier = new SentimentClassifier(config);
        assertEquals(Sentiment.POSITIVE.getLabel(), classifier.classify("유니크테스트 피드백"));

        config.removeSentimentKeyword(Sentiment.POSITIVE.getLabel(), "유니크테스트");
        classifier = new SentimentClassifier(config);
        Filters filters = new Filters(classifier);

        assertEquals(Sentiment.NEUTRAL.getLabel(), classifier.classify("유니크테스트 피드백"));
        List<Feedback> filtered = filters.filterFeedbacks(
                List.of(new Feedback("유니크테스트 피드백")), Sentiment.POSITIVE.getLabel(), "전체");
        assertTrue(filtered.isEmpty());
    }
}
