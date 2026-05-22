package com.example.demo.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeywordStore {

    private Map<String, List<String>> sentiment = new LinkedHashMap<>();
    private Map<String, Map<String, Object>> category = new LinkedHashMap<>();

    public Map<String, List<String>> getSentiment() {
        return sentiment;
    }

    public void setSentiment(Map<String, List<String>> sentiment) {
        this.sentiment = sentiment;
    }

    public Map<String, Map<String, Object>> getCategory() {
        return category;
    }

    public void setCategory(Map<String, Map<String, Object>> category) {
        this.category = category;
    }
}
