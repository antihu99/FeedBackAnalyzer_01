package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demo.Constants.CATEGORY_KEYWORDS;
import static com.example.demo.Constants.FILTER_ALL;

@Service
public class Filters {

    private final SentimentClassifier sentimentClassifier;

    @Autowired
    public Filters(SentimentClassifier sentimentClassifier) {
        this.sentimentClassifier = sentimentClassifier;
    }

    public List<Feedback> filterFeedbacks(List<Feedback> dataList, String sentimentFilter, String keywordFilter) {
        List<Feedback> afterSentiment = applySentimentFilter(dataList, sentimentFilter);
        List<Feedback> finalFiltered = applyKeywordFilter(afterSentiment, keywordFilter);

        for (Feedback item : finalFiltered) {
            System.out.println(item.getText());
        }

        return finalFiltered;
    }

    private List<Feedback> applySentimentFilter(List<Feedback> dataList, String sentimentFilter) {
        if (FILTER_ALL.equals(sentimentFilter)) {
            return new ArrayList<>(dataList);
        }
        List<Feedback> matched = new ArrayList<>();
        for (Feedback item : dataList) {
            if (sentimentClassifier.classify(item.getText()).equals(sentimentFilter)) {
                matched.add(item);
            }
        }
        return matched;
    }

    private List<Feedback> applyKeywordFilter(List<Feedback> dataList, String keywordFilter) {
        if (FILTER_ALL.equals(keywordFilter)) {
            return new ArrayList<>(dataList);
        }
        List<Feedback> matched = new ArrayList<>();
        for (Feedback item : dataList) {
            if (matchesCategoryKeyword(item.getText().toLowerCase(), keywordFilter)) {
                matched.add(item);
            }
        }
        return matched;
    }

    private boolean matchesCategoryKeyword(String lowerText, String keywordFilter) {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> subKeywords = (Map<String, List<String>>) CATEGORY_KEYWORDS
                .get(keywordFilter).get("sub");
        for (List<String> keywords : subKeywords.values()) {
            if (keywords.stream().anyMatch(lowerText::contains)) {
                return true;
            }
        }
        return false;
    }
}
