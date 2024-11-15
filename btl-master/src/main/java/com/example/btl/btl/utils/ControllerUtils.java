package com.example.btl.btl.utils;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControllerUtils {
    public static void ShowAlert(Model model, boolean isSuccess, String message) {
        model.addAttribute("alertType", "alert-" + (isSuccess ? "success" : "danger"));
        model.addAttribute("alertMessage", message);
    }
    public static void ShowAlertAfterRedirect(RedirectAttributes redirectAttributes, boolean isSuccess, String message) {
        redirectAttributes.addFlashAttribute("alertType", "alert-" + (isSuccess ? "success" : "danger"));
        redirectAttributes.addFlashAttribute("alertMessage", message);
    }
}
