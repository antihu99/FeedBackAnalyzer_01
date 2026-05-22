package com.example.demo;

import com.example.demo.model.TrendSeries;
import com.example.demo.service.TrendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrendServiceTest {

    private final TrendService trendService = new TrendService();

    @Test
    @DisplayName("TC-TREND-01: classpath CSV date별 집계")
    void loadTrendData_returnsSeriesByDate() {
        TrendSeries series = trendService.loadTrendSeries();

        assertFalse(series.getLabels().isEmpty());
        assertFalse(series.getCounts().isEmpty());
        assertEquals(series.getLabels().size(), series.getCounts().size());
        assertEquals(8, series.totalCount());
    }

    @Test
    @DisplayName("TC-TREND-02: 파일 없음 시 empty+message")
    void loadTrendData_missingFile_returnsEmptyWithMessage() {
        TrendSeries series = trendService.loadTrendSeriesFromPath(
                java.nio.file.Path.of("nonexistent-trend-file.csv"));

        assertTrue(series.getLabels().isEmpty());
        assertTrue(series.getCounts().isEmpty());
        assertNotNull(series.getMessage());
    }
}
