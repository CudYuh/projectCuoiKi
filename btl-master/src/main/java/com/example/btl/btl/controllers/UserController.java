package com.example.btl.btl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.btl.btl.dtos.AdminChangePassForm;
import com.example.btl.btl.dtos.UserChangePassForm;
import com.example.btl.btl.models.Admin;
import com.example.btl.btl.models.Order;
import com.example.btl.btl.models.User;
import com.example.btl.btl.services.CategoryService;
import com.example.btl.btl.services.OrderService;
import com.example.btl.btl.services.UserService;
import com.example.btl.btl.utils.ControllerUtils;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/dang-nhap")
    public String showLoginForm(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }

        model.addAttribute("content", "login");
        return "index";
    }

    @PostMapping("/dang-nhap")
    public String processLoginForm(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }

        User user = userService.getUserByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            ControllerUtils.ShowAlert(model, false, "Sai tài khoản hoặc mật khẩu");
            model.addAttribute("content", "login");
            return "index";
        }

        if (user.getStatus() != 1) {
            ControllerUtils.ShowAlert(model, false, "Tài khoản bị khóa");
            model.addAttribute("content", "login");
            return "index";
        }

        session.setAttribute("user", user);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đăng nhập thành công");
        return "redirect:/";
    }

    @GetMapping("/dang-xuat")
    public String processLogout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/dang-ky")
    public String showRegisterForm(Model model) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        model.addAttribute("user", new User());
        model.addAttribute("content", "register");
        return "index";
    }

    @PostMapping("/dang-ky")
    public String processRegisterForm(@ModelAttribute User user,
            RedirectAttributes redirectAttributes,
            Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        session.setAttribute("user", null);

        user.setStatus(1);

        userService.saveUser(user);

        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đăng ký thành công");
        return "redirect:/dang-nhap";
    }

    @GetMapping("/thong-tin-tai-khoan")
    public String thongTinTaiKhoan(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/dang-nhap";
        }

        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("data", orders);

        model.addAttribute("content", "userinfo");
        return "index";
    }

    @PostMapping("/cap-nhat-tai-khoan")
    public String updateAccount(@ModelAttribute("user") User userDetails, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/dang-nhap";
        }

        String userId = user.getId();
        try {
            User newUser = userService.updateUser(userId, userDetails, true);
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Cập nhật thông tin thành công");
            session.setAttribute("user", newUser);
        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, "Cập nhật thông tin không thành công");
        }
        return "redirect:/thong-tin-tai-khoan";
    }

    @GetMapping("/doi-mat-khau")
    public String changePass(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("userChangePassForm", new UserChangePassForm());
        model.addAttribute("content", "changepass_form");

        return "index";
    }

    @PostMapping("/doi-mat-khau")
    public String changePassword(Model model, HttpSession session,
            @ModelAttribute("userChangePassForm") UserChangePassForm userChangePassForm,RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        String id = ((User) session.getAttribute("user")).getId();

        if (!userChangePassForm.getNewPass().equals(userChangePassForm.getConfirmNewPass())) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, "Xác nhận mật khẩu mới không đúng");
            return "redirect:/doi-mat-khau";
        }

        try {
            userService.changePassword(id, userChangePassForm);
        } catch (Exception e) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, e.getMessage());
            return "redirect:/doi-mat-khau";
        }

        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đổi mật khẩu thành công");
        return "redirect:/thong-tin-tai-khoan";
    }
}