package com.example.demo;

import com.example.demo.model.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileHandler {
    public void saveResult(List<Feedback> data) {
        System.out.println("saveResult" + data.size());
        for (Feedback iter : data) {
            System.out.println(iter.getText());
        }
    }

    public void save(List<Feedback> data) {
        saveResult(data);
    }
}
