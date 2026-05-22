package com.example.demo;

import com.example.demo.model.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionTest {

    @BeforeEach
    void reset() {
        Session.initSessionStateUgly();
        Session.updateInternalData("current_feedbacks", new ArrayList<>());
    }

    @Test
    @DisplayName("COV-02: Session 초기화·피드백 조회")
    void session_initAndFeedbacks() {
        Session.initSessionStateUgly();
        List<Feedback> fromKey = Session.getOldDataFromSession("current_feedbacks");
        assertNotNull(fromKey);

        List<Feedback> current = new ArrayList<>();
        current.add(new Feedback("테스트"));
        Session.updateInternalData("current_feedbacks", current);
        assertEquals(1, Session.getCurrentFeedbacks().size());
    }
}
