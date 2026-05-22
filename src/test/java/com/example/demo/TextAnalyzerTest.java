package com.example.demo;

import com.example.demo.config.Constants;
import com.example.demo.model.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextAnalyzerTest {

    private TextAnalyzer textAnalyzer;

    @BeforeEach
    void setUp() {
        SentimentClassifier classifier = new SentimentClassifier();
        textAnalyzer = new TextAnalyzer(classifier);
    }

    @Test
    @DisplayName("TA-01: 긍정 키워드 집계")
    void sent_positiveKeyword_incrementsPositive() {
        List<Feedback> list = List.of(new Feedback("배송이 최고예요"));
        Map<String, Integer> result = textAnalyzer.analyzeSentiment(list);
        assertEquals(1, result.get("긍정"));
        assertEquals(0, result.get("부정"));
        assertEquals(0, result.get("중립"));
    }

    @Test
    @DisplayName("TA-02: 부정 키워드 집계")
    void sent_negativeKeyword_incrementsNegative() {
        List<Feedback> list = List.of(new Feedback("화가 나고 최악이에요"));
        Map<String, Integer> result = textAnalyzer.analyzeSentiment(list);
        assertEquals(1, result.get("부정"));
    }

    @Test
    @DisplayName("TA-03: 키워드 없으면 중립 기본값")
    void sent_noKeyword_defaultsNeutral() {
        List<Feedback> list = List.of(new Feedback("보통이에요"));
        Map<String, Integer> result = textAnalyzer.analyzeSentiment(list);
        assertEquals(1, result.get("중립"));
    }

    @Test
    @DisplayName("TA-04: 빈 목록은 모두 0")
    void sent_emptyList_allZero() {
        Map<String, Integer> result = textAnalyzer.analyzeSentiment(Collections.emptyList());
        assertEquals(0, result.get("긍정"));
        assertEquals(0, result.get("부정"));
        assertEquals(0, result.get("중립"));
    }

    @Test
    @DisplayName("TA-05: 혼합 감정 각 1건")
    void sent_mixedCounts_correct() {
        List<Feedback> list = Arrays.asList(
                new Feedback("배송이 최고예요"),
                new Feedback("화가 나고 최악이에요"),
                new Feedback("보통이에요"));
        Map<String, Integer> result = textAnalyzer.analyzeSentiment(list);
        assertEquals(1, result.get("긍정"));
        assertEquals(1, result.get("부정"));
        assertEquals(1, result.get("중립"));
        assertEquals(3, result.get("긍정") + result.get("부정") + result.get("중립"));
    }

    @Test
    @DisplayName("TA-06: 배송 카테고리 키워드")
    void kw_deliveryCategory_matches() {
        List<Feedback> list = List.of(new Feedback("택배 배송이 지연됐어요"));
        Map<String, Integer> result = textAnalyzer.analyzeKeywords(list);
        assertTrue(result.get("배송") >= 1);
    }

    @Test
    @DisplayName("TA-07: 품질 카테고리 키워드")
    void kw_qualityCategory_matches() {
        List<Feedback> list = List.of(new Feedback("품질이 별로예요"));
        Map<String, Integer> result = textAnalyzer.analyzeKeywords(list);
        assertTrue(result.get("품질") >= 1);
    }

    @Test
    @DisplayName("TA-08: 빈 목록 카테고리 0")
    void kw_emptyList_allZero() {
        Map<String, Integer> result = textAnalyzer.analyzeKeywords(Collections.emptyList());
        for (String category : Constants.CATEGORY_KEYWORDS.keySet()) {
            assertEquals(0, result.get(category));
        }
    }
}
