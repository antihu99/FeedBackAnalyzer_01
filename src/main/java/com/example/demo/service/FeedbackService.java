package com.example.demo.service;

import com.example.demo.Filters;
import com.example.demo.Logger;
import com.example.demo.Session;
import com.example.demo.TextAnalyzer;
import com.example.demo.UIComponents;
import com.example.demo.model.Feedback;
import com.opencsv.CSVReader;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    @Autowired
    private TextAnalyzer textAnalyzer;

    @Autowired
    private Filters filters;

    @Autowired
    private UIComponents uiComponents;

    @Autowired
    private Logger logger;

    private List<Feedback> filteredFeedbacksForExport = new ArrayList<>();

    public void prepareIndex(Model model) {
        Session.initSessionStateUgly();
        List<Feedback> feedbacks = Session.getOldDataFromSession("current_feedbacks");
        model.addAttribute("success", "피드백 분석기 시작");
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("categories", uiComponents.getCategories());
    }

    public void analyzeFeedback(String text, Model model) {
        try {
            List<Feedback> feedbacks = Session.getCurrentFeedbacks();

            if (text != null && !text.trim().isEmpty()) {
                feedbacks.add(new Feedback(text.trim()));
            }

            for (Feedback feedback : feedbacks) {
                logger.logInfo("%s", feedback.getText());
            }

            Session.updateInternalData("current_feedbacks", feedbacks);
            logger.logInfo("현재 %d개의 피드백이 입력되었습니다.", feedbacks.size());

            model.addAttribute("success", feedbacks.size() + "개의 피드백이 입력되었습니다.");
            if (!feedbacks.isEmpty()) {
                attachAnalysisResults(model, feedbacks, false);
                logger.logInfo("감성 분석 완료");
                logger.logInfo("키워드 분석 완료");
            }
        } catch (Exception e) {
            logger.logError("오류 발생: %s", e.getMessage());
            model.addAttribute("error", "처리 중 오류가 발생했습니다.");
        }
    }

    public void uploadCsv(MultipartFile file, Model model) {
        try {
            if (!file.isEmpty()) {
                List<Feedback> feedbacks = Session.getCurrentFeedbacks();
                parseCsvIntoFeedbacks(file, feedbacks);
                Session.updateInternalData("current_feedbacks", feedbacks);
                model.addAttribute("success", feedbacks.size() + "개의 피드백이 입력되었습니다.");
                model.addAttribute("feedbacks", feedbacks);
                model.addAttribute("categories", uiComponents.getCategories());
                logger.logInfo("파일이 성공적으로 업로드되었습니다.");
            }
        } catch (Exception e) {
            logger.logError("파일 업로드 오류: %s", e.getMessage());
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
        }
    }

    public void filterFeedbacks(String sentiment, String keyword, Model model) {
        try {
            List<Feedback> feedbacks = Session.getCurrentFeedbacks();

            if (feedbacks.isEmpty()) {
                logger.logWarning("분석할 피드백이 없습니다.");
                model.addAttribute("warning", "분석할 피드백이 없습니다.");
                return;
            }

            List<Feedback> filtered = filters.filterFeedbacks(feedbacks, sentiment, keyword);

            if (filtered.isEmpty()) {
                model.addAttribute("categories", uiComponents.getCategories());
                logger.logWarning("필터링 결과가 없습니다.");
                model.addAttribute("warning", "필터링 결과가 없습니다.");
                return;
            }

            filteredFeedbacksForExport = filtered;
            attachAnalysisResults(model, filtered, true);
            logger.logInfo("필터링 결과: %d개의 피드백", filtered.size());
        } catch (Exception e) {
            logger.logError("오류 발생: %s", e.getMessage());
            model.addAttribute("error", "처리 중 오류가 발생했습니다.");
        }
    }

    public void writeFilteredCsv(HttpServletResponse response) throws IOException {
        String fn = "filtered_feedback.csv; charset=UTF-8";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment:filename=\"" + fn + "\"");

        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        response.getOutputStream().write(bom);

        PrintWriter writer = new PrintWriter(response.getOutputStream(), true, StandardCharsets.UTF_8);
        writer.println("text");
        for (Feedback feedback : filteredFeedbacksForExport) {
            writer.println(feedback.getText());
        }
        writer.flush();
        writer.close();
    }

    private void attachAnalysisResults(Model model, List<Feedback> feedbacks, boolean filteredView) {
        Map<String, Integer> sentimentResults = textAnalyzer.analyzeSentiment(feedbacks);
        Map<String, Integer> keywordResults = textAnalyzer.analyzeKeywords(feedbacks);
        model.addAttribute("sentimentResults", sentimentResults);
        model.addAttribute("keywordResults", keywordResults);
        model.addAttribute("categories", uiComponents.getCategories());
        if (filteredView) {
            model.addAttribute("filteredFeedbacks", feedbacks);
        } else {
            model.addAttribute("feedbacks", feedbacks);
        }
    }

    private void parseCsvIntoFeedbacks(MultipartFile file, List<Feedback> feedbacks) throws IOException {
        File tmpFile = new File("C:\\tmp\\" + file.getOriginalFilename());
        file.transferTo(tmpFile);
        try (CSVReader csvReader = new CSVReader(new FileReader(tmpFile))) {
            csvReader.readNext();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                feedbacks.add(new Feedback(line[0]));
            }
        } catch (Exception e) {
            logger.logError("csv 파일 처리 오류: %s", e.getMessage());
            throw new RuntimeException(e);
        }
        tmpFile.deleteOnExit();
    }
}
