package com.example.demo;

import com.example.demo.service.KeywordConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UIComponents {

    private final KeywordConfigService keywordConfig;

    @Autowired
    public UIComponents(KeywordConfigService keywordConfig) {
        this.keywordConfig = keywordConfig;
    }

    public List<String> getCategories() {
        return keywordConfig.getCategoryNames();
    }
}
