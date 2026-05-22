package com.example.demo.config;

/**
 * 감정 분류 라벨 (FR-13). UI·필터 파라미터와 동일한 한글 문자열.
 */
public enum Sentiment {
    POSITIVE("긍정"),
    NEGATIVE("부정"),
    NEUTRAL("중립");

    private final String label;

    Sentiment(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
