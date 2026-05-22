package com.example.demo;

import com.example.demo.config.Sentiment;
import com.example.demo.model.Feedback;
import com.example.demo.service.KeywordConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TextAnalyzer {

    private static Map<String, Integer> globalSent = null;
    private static Map<String, Integer> globalKw = null;

    private final SentimentClassifier sentimentClassifier;
    private final KeywordConfigService keywordConfig;

    @Autowired
    public TextAnalyzer(SentimentClassifier sentimentClassifier, KeywordConfigService keywordConfig) {
        this.sentimentClassifier = sentimentClassifier;
        this.keywordConfig = keywordConfig;
    }

    /** 기존 단위 테스트 호환 */
    public TextAnalyzer(SentimentClassifier sentimentClassifier) {
        this(sentimentClassifier, KeywordConfigService.createInMemoryFromConstants());
    }

    public Map<String, Integer> analyzeSentiment(List<Feedback> feedbacks) {
        Map<String, Integer> counts = newEmptySentimentCounts();

        for (Feedback feedback : feedbacks) {
            String label = sentimentClassifier.classify(feedback.getText());
            counts.put(label, counts.get(label) + 1);
        }

        globalSent = counts;
        return counts;
    }

    public Map<String, Integer> analyzeKeywords(List<Feedback> feedbacks) {
        Map<String, Integer> counts = newEmptyCategoryCounts();

        for (Feedback feedback : feedbacks) {
            incrementMatchingCategories(counts, feedback.getText().toLowerCase());
        }

        globalKw = counts;
        return counts;
    }

    private static Map<String, Integer> newEmptySentimentCounts() {
        Map<String, Integer> counts = new HashMap<>();
        for (Sentiment sentiment : Sentiment.values()) {
            counts.put(sentiment.getLabel(), 0);
        }
        return counts;
    }

    private Map<String, Integer> newEmptyCategoryCounts() {
        Map<String, Integer> counts = new HashMap<>();
        for (String category : keywordConfig.getCategoryKeywords().keySet()) {
            counts.put(category, 0);
        }
        return counts;
    }

    private void incrementMatchingCategories(Map<String, Integer> counts, String lowerText) {
        for (Map.Entry<String, Map<String, Object>> entry : keywordConfig.getCategoryKeywords().entrySet()) {
            String category = entry.getKey();
            @SuppressWarnings("unchecked")
            List<String> mainKeywords = (List<String>) entry.getValue().get("main");
            if (mainKeywords.stream().anyMatch(keyword -> lowerText.contains(keyword))) {
                counts.put(category, counts.get(category) + 1);
            }
        }
    }
}
