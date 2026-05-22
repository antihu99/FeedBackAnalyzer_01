package com.example.demo;

import com.example.demo.config.Constants;
import com.example.demo.config.Sentiment;
import org.springframework.stereotype.Service;

/**
 * 감정 판별 단일 책임 (FR-14). TextAnalyzer·Filters가 동일 규칙을 위임한다.
 */
@Service
public class SentimentClassifier {

    public String classify(String text) {
        String lowerText = text.toLowerCase();
        if (Constants.SENTIMENT_KEYWORDS.get(Sentiment.POSITIVE.getLabel()).stream()
                .anyMatch(lowerText::contains)) {
            return Sentiment.POSITIVE.getLabel();
        }
        if (Constants.SENTIMENT_KEYWORDS.get(Sentiment.NEGATIVE.getLabel()).stream()
                .anyMatch(lowerText::contains)) {
            return Sentiment.NEGATIVE.getLabel();
        }
        return Sentiment.NEUTRAL.getLabel();
    }
}
