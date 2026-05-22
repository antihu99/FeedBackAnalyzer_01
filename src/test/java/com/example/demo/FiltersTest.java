package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiltersTest {

    private TextAnalyzer textAnalyzer;
    private Filters filters;

    @BeforeEach
    void setUp() {
        textAnalyzer = new TextAnalyzer();
        filters = new Filters();
    }

    @Test
    @DisplayName("FI-01: 긍정 필터만")
    void fil_positive_only() {
        List<Feedback> list = List.of(new Feedback("정말 만족합니다"));
        List<Feedback> result = filters.fil(list, "긍정", "전체");
        assertEquals(1, result.size());
        assertEquals("정말 만족합니다", result.get(0).getText());
    }

    @Test
    @DisplayName("FI-02: 부정 필터만")
    void fil_negative_only() {
        List<Feedback> list = List.of(new Feedback("불만이에요"));
        List<Feedback> result = filters.fil(list, "부정", "전체");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("FI-03: 중립 키워드 보통")
    void fil_neutral_keyword보통() {
        List<Feedback> list = List.of(new Feedback("보통이에요"));
        List<Feedback> result = filters.fil(list, "중립", "전체");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("FI-04: 전체 감정 필터")
    void fil_all_sentiment() {
        List<Feedback> list = Arrays.asList(
                new Feedback("배송이 최고예요"),
                new Feedback("불만이에요"),
                new Feedback("보통이에요"));
        List<Feedback> result = filters.fil(list, "전체", "전체");
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("FI-05: 배송 키워드 필터")
    void fil_keyword_delivery() {
        List<Feedback> list = List.of(new Feedback("택배 배송 지연"));
        List<Feedback> result = filters.fil(list, "전체", "배송");
        assertTrue(result.size() >= 1, "배송 sub 키워드 매칭 (중복 add 가능)");
    }

    @Test
    @DisplayName("FI-06: 전체 키워드 필터")
    void fil_keyword_all() {
        List<Feedback> list = Arrays.asList(
                new Feedback("택배 배송 지연"),
                new Feedback("품질이 별로예요"));
        List<Feedback> result = filters.fil(list, "전체", "전체");
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("FI-07: 감정+키워드 복합")
    void fil_combined_sentimentAndKeyword() {
        List<Feedback> list = List.of(new Feedback("배송이 빨라서 최고예요"));
        List<Feedback> result = filters.fil(list, "긍정", "배송");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("FI-08: 빈 목록")
    void fil_emptyList_returnsEmpty() {
        List<Feedback> result = filters.fil(Collections.emptyList(), "중립", "전체");
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("TC-NEUTRAL-01: 분석 중립 vs 중립 필터 일치 (RED FAIL)")
    void sentimentAnalysis_matchesNeutralFilter() {
        List<Feedback> data = List.of(new Feedback("괜찮은 서비스였어요"));
        Map<String, Integer> sent = textAnalyzer.sent(data);
        assertEquals(1, sent.get("중립"), "TextAnalyzer는 괜찮을 중립으로 집계");

        List<Feedback> filtered = filters.fil(data, "중립", "전체");
        assertEquals(1, filtered.size(),
                "Filters 중립 필터도 동일 건을 포함해야 함 (GREEN에서 수정)");
    }

    @Test
    @DisplayName("TC-NEUTRAL-02: 중립 집계 수 vs 필터 건수 (RED FAIL)")
    void mixedList_filterNeutralCount_matchesSentimentBucket() {
        List<Feedback> all = Arrays.asList(
                new Feedback("최고예요"),
                new Feedback("최악이에요"),
                new Feedback("괜찮은 서비스"),
                new Feedback("보통이에요"));
        Map<String, Integer> sent = textAnalyzer.sent(all);
        int neutralBucket = sent.get("중립");
        int filteredSize = filters.fil(all, "중립", "전체").size();
        assertEquals(neutralBucket, filteredSize,
                "sent() 중립 건수와 fil(중립) 결과 건수가 일치해야 함");
    }
}
