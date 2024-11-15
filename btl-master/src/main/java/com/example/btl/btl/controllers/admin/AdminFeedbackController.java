package com.example.btl.btl.controllers.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.btl.btl.models.Feedback;
import com.example.btl.btl.services.FeedbackService;
import com.example.btl.btl.utils.ControllerUtils;

@Controller
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "status", "createdAt"));
        Page<Feedback> dataPage = feedbackService.getAllFeedbacks(pageable);

        model.addAttribute("data", dataPage.getContent());
        model.addAttribute("currentPage", dataPage.getNumber() + 1);
        model.addAttribute("totalPages", dataPage.getTotalPages());
        model.addAttribute("totalItems", dataPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("contentName", "Feedback Management");
        model.addAttribute("content", "admin/feedback");

        return "admin/index";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable int id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        feedbackService.changeStatus(id, 0);
        model.addAttribute("feedback", feedbackService.getFeedbackById(id));
        model.addAttribute("contentName", "Feedback Detail");
        model.addAttribute("content", "admin/feedback_detail");

        return "admin/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        feedbackService.deleteFeedback(id);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Xóa thành công");
        return "redirect:/admin/feedback";
    }

}
