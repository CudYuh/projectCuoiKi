package com.example.btl.btl.controllers.admin;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.btl.btl.dtos.AdminChangePassForm;
import com.example.btl.btl.dtos.AdminLoginForm;
import com.example.btl.btl.models.Admin;
import com.example.btl.btl.services.AdminService;
import com.example.btl.btl.utils.ControllerUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        List<Admin> list = adminService.findAllAdmins();
        if (list.isEmpty()) {
            System.out.println("Not found");
            Admin admin = new Admin();
            admin.setName("admin");
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setRole(1);
            admin.setAddress("admin");
            admin.setPhone("admin");
            admin.setStatus(1);
            adminService.saveAdmin(admin);
        }
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("content", "admin/home");
        return "admin/index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "redirect:/admin";
        }

        model.addAttribute("adminLoginForm", new AdminLoginForm());
        return "admin/login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute("adminLoginForm") AdminLoginForm adminLoginForm,
                                   BindingResult bindingResult, HttpSession session,Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") != null) {
            return "admin/login";
        }

        if (bindingResult.hasErrors()) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "admin/login";
        }

        Admin admin = adminService.getAdminByUsername(adminLoginForm.getUsername());
//        mypassword = encoder.encode(admin.getPassword());
        if (admin == null || !passwordEncoder.matches(adminLoginForm.getPassword(), admin.getPassword())) {
            ControllerUtils.ShowAlert(model, false, "Sai tài khoản hoặc mật khẩu");
            return "admin/login";
        }

        if (admin.getStatus() != 1) {
            ControllerUtils.ShowAlert(model, false, "Tài khoản bị khóa");
            return "admin/login";
        }

        session.setAttribute("admin", admin);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đăng nhập thành công");
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        session.removeAttribute("admin");
        return "redirect:/admin/login";
    }

    @GetMapping("/changepass")
    public String changePass(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("adminChangePassForm", new AdminChangePassForm());
        model.addAttribute("content", "admin/changepass_form");

        return "admin/index";
    }

    @PostMapping("/changepass")
    public String changePassword(Model model, HttpSession session,
                                 @ModelAttribute("adminChangePassForm") AdminChangePassForm adminChangePassForm, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        String id = ((Admin) session.getAttribute("admin")).getId();

        if (!adminChangePassForm.getNewPass().equals(adminChangePassForm.getConfirmNewPass())) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, "Xác nhận mật khẩu mới không đúng");
            return "redirect:/admin/changepass";
        }

        try {
            adminService.changePassword(id, adminChangePassForm);
        } catch (Exception e) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, e.getMessage());
            return "redirect:/admin/changepass";
        }

        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đổi mật khẩu thành công");
        return "redirect:/admin";
    }
}