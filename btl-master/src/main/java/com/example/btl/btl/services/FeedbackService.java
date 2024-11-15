package com.example.btl.btl.services;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.btl.btl.models.Feedback;
import com.example.btl.btl.repositories.FeedbackRepo;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepo feedbackRepo;

    public Page<Feedback> getAllFeedbacks(Pageable pageable) {
        return feedbackRepo.findAll(pageable);
    }

    public Feedback getFeedbackById(int id) {
        Feedback feedback = feedbackRepo.findById(id).orElse(null);
        return feedback; // tìm fb với id tương ứng, k thấy trả về null , thấy trả về fb yêu cầu
    }

    public Feedback saveFeedback(Feedback feedback) {
        feedback.setCreatedAt(new Timestamp(System.currentTimeMillis())); //thời gian tạo fb
        feedback.setStatus(1);
        return feedbackRepo.save(feedback);
    }

    public void deleteFeedback(int id) {
        feedbackRepo.deleteById(id);
    }

    public void changeStatus(int id, int status) {
        Feedback feedback = feedbackRepo.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setStatus(status);
            feedbackRepo.save(feedback);
        }
    }
}