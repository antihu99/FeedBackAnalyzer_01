package com.example.demo.service;

import com.example.demo.model.TrendSeries;
import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrendService {

    public static final String TREND_CSV_RESOURCE = "test_feedback_trend.csv";

    public TrendSeries loadTrendSeries() {
        ClassPathResource resource = new ClassPathResource(TREND_CSV_RESOURCE);
        if (!resource.exists()) {
            return TrendSeries.empty("Trend CSV 파일이 없습니다.");
        }
        try {
            return parseCsv(new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return TrendSeries.empty("Trend CSV를 읽을 수 없습니다: " + e.getMessage());
        }
    }

    public TrendSeries loadTrendSeriesFromPath(Path path) {
        if (path == null || !Files.exists(path)) {
            return TrendSeries.empty("Trend CSV 파일이 없습니다.");
        }
        try {
            return parseCsv(new CSVReader(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return TrendSeries.empty("Trend CSV를 읽을 수 없습니다: " + e.getMessage());
        }
    }

    private TrendSeries parseCsv(CSVReader reader) throws Exception {
        Map<String, Integer> byDate = new LinkedHashMap<>();
        reader.readNext();
        String[] line;
        int rows = 0;
        while ((line = reader.readNext()) != null) {
            if (line.length < 2 || line[0].isBlank()) {
                continue;
            }
            String date = line[0].trim();
            byDate.merge(date, 1, Integer::sum);
            rows++;
        }
        if (rows == 0) {
            return TrendSeries.empty("Trend 데이터가 비어 있습니다.");
        }
        List<String> labels = new ArrayList<>(byDate.keySet());
        List<Integer> counts = new ArrayList<>();
        for (String label : labels) {
            counts.add(byDate.get(label));
        }
        return new TrendSeries(labels, counts, null);
    }
}
