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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.btl.btl.models.Admin;
import com.example.btl.btl.models.User;
import com.example.btl.btl.services.UserService;
import com.example.btl.btl.utils.ControllerUtils;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<User> dataPage = userService.getAllUsers(pageable);

        model.addAttribute("data", dataPage.getContent());
        model.addAttribute("currentPage", dataPage.getNumber() + 1);
        model.addAttribute("totalPages", dataPage.getTotalPages());
        model.addAttribute("totalItems", dataPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("contentName", "User Management");
        model.addAttribute("content", "admin/user");

        return "admin/index";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("user", new User());

        model.addAttribute("contentName", "Thêm User");
        model.addAttribute("content", "admin/user_form_add");
        return "admin/index";
    }

    @PostMapping("/add")
    public String doAdd(@ModelAttribute("user") User user, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        user.setCreatedBy(((Admin) session.getAttribute("admin")).getId());
        user.setStatus(1);

        userService.saveUser(user);

        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Thêm thành công");
        return "redirect:/admin/user";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("contentName", "Edit User has ID = " + id);
        model.addAttribute("content", "admin/user_form_edit");

        return "admin/index";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable String id, @ModelAttribute("user") User user, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        user.setId(id);
        userService.updateUser(id, user, false);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Sửa thành công");
        return "redirect:/admin/user";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        userService.deleteUser(id);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Xóa thành công");
        return "redirect:/admin/user";

    }

    @GetMapping("/change-status/{id}")
    public String changeStatus(@PathVariable("id") String id, @RequestParam int status, HttpSession session,
            @RequestParam(name = "page", defaultValue = "1") String page,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        try {
            userService.updateStatus(id, status, ((Admin) session.getAttribute("admin")).getId());
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Sửa trạng thái thành công");
        } catch (Exception e) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Sửa trạng thái không thành công");
            e.printStackTrace();
        }
        return "redirect:/admin/user?page=" + page;
    }

}