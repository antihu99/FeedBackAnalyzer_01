package com.example.demo;

import com.example.demo.service.KeywordConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UIComponentsTest {

    @Test
    @DisplayName("COV: UIComponents 카테고리 목록")
    void getCategories_returnsFive() {
        UIComponents ui = new UIComponents(KeywordConfigService.createInMemoryFromConstants());
        @SuppressWarnings("unchecked")
        List<String> cats = (List<String>) ui.getCategories();
        assertEquals(5, cats.size());
    }
}
