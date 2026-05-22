package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void resetSession() {
        Session.initSessionStateUgly();
        Session.updateInternalData("current_feedbacks", new ArrayList<>());
    }

    @Test
    @DisplayName("COV-03: GET / 대시보드")
    void index_returnsOk() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("COV: POST /analyze")
    void analyze_withText() throws Exception {
        mockMvc.perform(post("/analyze").param("text", "배송이 최고예요"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("COV: POST /filter 긍정")
    void filter_positive() throws Exception {
        mockMvc.perform(post("/analyze").param("text", "배송이 최고예요"));
        mockMvc.perform(post("/filter")
                        .param("sentiment", "긍정")
                        .param("keyword", "전체"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("COV: POST /filter 결과 없음")
    void filter_emptyResult() throws Exception {
        mockMvc.perform(post("/analyze").param("text", "보통이에요"));
        mockMvc.perform(post("/filter")
                        .param("sentiment", "부정")
                        .param("keyword", "전체"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("COV: GET /download")
    void download_afterFilter() throws Exception {
        mockMvc.perform(post("/analyze").param("text", "배송이 최고예요"));
        mockMvc.perform(post("/filter")
                .param("sentiment", "긍정")
                .param("keyword", "전체"));
        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    @DisplayName("COV: POST /filter 피드백 없음")
    void filter_noFeedbacks() throws Exception {
        mockMvc.perform(post("/filter")
                        .param("sentiment", "긍정")
                        .param("keyword", "전체"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("COV: POST /analyze 빈 텍스트")
    void analyze_emptyText() throws Exception {
        mockMvc.perform(post("/analyze").param("text", "   "))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("COV: POST /upload CSV")
    void upload_csv() throws Exception {
        String csv = "text\n배송이 빨라요\n";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));
        new File("C:\\tmp").mkdirs();
        mockMvc.perform(multipart("/upload").file(file))
                .andExpect(status().isOk());
    }
}
