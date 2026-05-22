package com.example.demo.model;

import java.util.Collections;
import java.util.List;

public class TrendSeries {

    private final List<String> labels;
    private final List<Integer> counts;
    private final String message;

    public TrendSeries(List<String> labels, List<Integer> counts, String message) {
        this.labels = labels == null ? List.of() : List.copyOf(labels);
        this.counts = counts == null ? List.of() : List.copyOf(counts);
        this.message = message;
    }

    public static TrendSeries empty(String message) {
        return new TrendSeries(Collections.emptyList(), Collections.emptyList(), message);
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Integer> getCounts() {
        return counts;
    }

    public String getMessage() {
        return message;
    }

    public int totalCount() {
        return counts.stream().mapToInt(Integer::intValue).sum();
    }
}
