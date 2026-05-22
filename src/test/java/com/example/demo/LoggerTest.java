package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerTest {

    @Test
    @DisplayName("COV: Logger 레벨 출력")
    void logger_levels() {
        assertDoesNotThrow(() -> {
            Logger.logInfo("info");
            Logger.logWarning("warn");
            Logger.logError("error");
            Logger.logDebug("debug");
            Logger.logInfo("fmt %s", "arg");
        });
        Logger.setDebugMode(true);
        assertTrue(Logger.isDebugMode());
    }
}
