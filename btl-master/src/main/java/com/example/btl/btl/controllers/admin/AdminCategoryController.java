package com.example.btl.btl.controllers.admin;

import java.io.IOException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.btl.btl.models.Admin;
import com.example.btl.btl.models.Category;
import com.example.btl.btl.services.CategoryService;
import com.example.btl.btl.utils.ControllerUtils;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        int size = 10;

        Page<Category> dataPage = categoryService.getAllCategories(page,size);

        model.addAttribute("data", dataPage.getContent());
        model.addAttribute("currentPage", dataPage.getNumber() + 1);
        model.addAttribute("totalPages", dataPage.getTotalPages());
        model.addAttribute("totalItems", dataPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("contentName", "Category Management");
        model.addAttribute("content", "admin/category");

        return "admin/index";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("category", new Category());

        model.addAttribute("contentName", "Thêm danh mục");
        model.addAttribute("content", "admin/category_form_add");
        return "admin/index";
    }

    @PostMapping("/add")
    public String doAdd(@ModelAttribute("category") Category category, HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        MultipartFile imageFile = category.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImage(imageFile.getBytes());
        }
        category.setCreatedBy(((Admin) session.getAttribute("admin")).getId());
        category.setStatus(1);

        categoryService.saveCategory(category);

        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Thêm thành công");
        return "redirect:/admin/category";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("contentName", "Edit Create has ID = " + id);
        model.addAttribute("content", "admin/category_form_edit");

        return "admin/index";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable int id, @ModelAttribute("category") Category category, HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        MultipartFile imageFile = category.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImage(imageFile.getBytes());
        }
        category.setId(id);
        category.setLastUpdatedBy(((Admin) session.getAttribute("admin")).getId());
        categoryService.updateCategory(id, category);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Sửa thành công");
        return "redirect:/admin/category";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        categoryService.deleteCategory(id);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Xóa thành công");
        return "redirect:/admin/category";
    }

    @GetMapping("/change-status/{id}")
    public String changeStatus(@PathVariable("id") int id,@RequestParam int status, HttpSession session,@RequestParam(name = "page", defaultValue = "1") String page,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        try {
            categoryService.changeStatus(id, status, ((Admin)session.getAttribute("admin")).getId());
        } catch (Exception e) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false, e.getMessage());
        }
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đã đổi trạng thái");
        return "redirect:/admin/category?page=" + page;
    }
    @GetMapping("/category/image/{id}")
    @ResponseBody
    public ResponseEntity<String> getCategoryImage(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            // Trả về ảnh dưới dạng Base64, loại content là text/plain
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain") // Đảm bảo content-type là text/plain
                    .body(category.base64Image());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");  // Nếu không tìm thấy category, trả về mã lỗi 404
    }
}
