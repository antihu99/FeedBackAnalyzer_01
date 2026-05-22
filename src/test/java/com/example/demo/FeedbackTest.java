package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedbackTest {

    @Test
    @DisplayName("COV-01: Feedback 생성·getter")
    void feedback_textGetter() {
        Feedback empty = new Feedback();
        assertEquals(null, empty.getText());

        Feedback f = new Feedback("배송 문의");
        assertEquals("배송 문의", f.getText());

        Feedback full = new Feedback("text", "긍정", "배송");
        assertEquals("text", full.getText());
    }
}
