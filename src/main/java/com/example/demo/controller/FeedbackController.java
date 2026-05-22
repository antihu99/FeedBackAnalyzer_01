package com.example.demo.controller;

import com.example.demo.service.FeedbackService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/")
    public String index(Model model) {
        feedbackService.prepareIndex(model);
        return "index";
    }

    @PostMapping("/analyze")
    public String analyze(@RequestParam("text") String text, Model model) {
        feedbackService.analyzeFeedback(text, model);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        feedbackService.uploadCsv(file, model);
        return "index";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam("sentiment") String sentiment,
                         @RequestParam("keyword") String keyword,
                         Model model) {
        feedbackService.filterFeedbacks(sentiment, keyword, model);
        return "index";
    }

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response) throws IOException {
        feedbackService.writeFilteredCsv(response);
    }

    @PostMapping("/keywords/add")
    public String addKeyword(@RequestParam("sentiment") String sentiment,
                             @RequestParam("keyword") String keyword,
                             Model model) {
        feedbackService.addSentimentKeyword(sentiment, keyword, model);
        return "index";
    }

    @PostMapping("/keywords/remove")
    public String removeKeyword(@RequestParam("sentiment") String sentiment,
                                @RequestParam("keyword") String keyword,
                                Model model) {
        feedbackService.removeSentimentKeyword(sentiment, keyword, model);
        return "index";
    }
}
