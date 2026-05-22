package com.example.demo;

import com.example.demo.config.Sentiment;
import com.example.demo.service.KeywordConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 감정 판별 단일 책임 (FR-14). TextAnalyzer·Filters가 동일 규칙을 위임한다.
 */
@Service
public class SentimentClassifier {

    private final KeywordConfigService keywordConfig;

    @Autowired
    public SentimentClassifier(KeywordConfigService keywordConfig) {
        this.keywordConfig = keywordConfig;
    }

    /** 기존 단위 테스트 호환: in-memory Constants 시드 */
    public SentimentClassifier() {
        this(KeywordConfigService.createInMemoryFromConstants());
    }

    public String classify(String text) {
        String lowerText = text.toLowerCase();
        if (containsAny(lowerText, keywordConfig.getSentimentKeywords()
                .getOrDefault(Sentiment.POSITIVE.getLabel(), List.of()))) {
            return Sentiment.POSITIVE.getLabel();
        }
        if (containsAny(lowerText, keywordConfig.getSentimentKeywords()
                .getOrDefault(Sentiment.NEGATIVE.getLabel(), List.of()))) {
            return Sentiment.NEGATIVE.getLabel();
        }
        return Sentiment.NEUTRAL.getLabel();
    }

    private static boolean containsAny(String lowerText, List<String> keywords) {
        return keywords.stream().anyMatch(lowerText::contains);
    }
}
