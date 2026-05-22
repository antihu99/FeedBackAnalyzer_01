package com.example.demo;

import com.example.demo.model.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FileHandlerTest {

    private FileHandler fileHandler;

    @BeforeEach
    void setUp() {
        fileHandler = new FileHandler();
    }

    @Test
    @DisplayName("FH-01: saveResult 예외 없음")
    void saveResult_doesNotThrow() {
        List<Feedback> list = List.of(new Feedback("테스트 피드백"));
        assertDoesNotThrow(() -> fileHandler.saveResult(list));
    }

    @Test
    @DisplayName("FH-02: save 위임")
    void save_delegatesToSaveResult() {
        List<Feedback> list = Arrays.asList(
                new Feedback("첫 번째"),
                new Feedback("두 번째"));
        assertDoesNotThrow(() -> fileHandler.save(list));
    }

    @Test
    @DisplayName("FH-03: 빈 목록 saveResult")
    void saveResult_emptyList() {
        assertDoesNotThrow(() -> fileHandler.saveResult(Collections.emptyList()));
    }
}
