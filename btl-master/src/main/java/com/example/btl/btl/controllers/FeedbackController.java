package com.example.btl.btl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.btl.btl.models.Feedback; //lấy thông tin từ mô hình models
import com.example.btl.btl.services.FeedbackService;
import com.example.btl.btl.utils.ControllerUtils;

@Controller
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService; //annotation Autowired để tiêm phụ thuộc của FBservice (xử lí data phản hồi của FB)

    @GetMapping("/lien-he")
    public String get(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("content", "feedback");
        return "index";
    }

    @PostMapping("/lien-he")
    public String submitFeedback(@ModelAttribute("feedback") Feedback feedback,RedirectAttributes redirectAttributes) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        //gọi đến svfb để bảo repo lưu fb
        if (savedFeedback != null) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true,
                    "Gửi thành công, chúng tôi sẽ sớm phản hồi");
        } else {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, "Gửi không thành công, vui lòng thử lại");
        } //ControllerUtils để đặt thông báo (alert) vào RedirectAttributes dựa trên kết quả
        return "redirect:/lien-he";
    }
}
