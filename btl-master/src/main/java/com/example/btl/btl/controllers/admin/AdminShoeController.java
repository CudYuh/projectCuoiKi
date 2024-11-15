package com.example.btl.btl.controllers.admin;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.btl.btl.dtos.ShoeFullDetail;
import com.example.btl.btl.models.Admin;
import com.example.btl.btl.models.Shoe;
import com.example.btl.btl.services.CategoryService;
import com.example.btl.btl.services.ShoeService;
import com.example.btl.btl.utils.ControllerUtils;

@Controller
@RequestMapping("/admin/shoe")
public class AdminShoeController {

    @Autowired
    private ShoeService shoeService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<Shoe> dataPage = shoeService.getAllShoes(pageable);

        model.addAttribute("data", dataPage.getContent());
        model.addAttribute("currentPage", dataPage.getNumber() + 1);
        model.addAttribute("totalPages", dataPage.getTotalPages());
        model.addAttribute("totalItems", dataPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("contentName", "Shoe Management");
        model.addAttribute("content", "admin/shoe");

        return "admin/index";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("shoe", new Shoe());

        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(-1, -1));
        model.addAttribute("contentName", "Thêm sản phẩm");
        model.addAttribute("content", "admin/shoe_form_add");
        return "admin/index";
    }

    @PostMapping("/add")
    public ResponseEntity<String> doAdd(@RequestBody ShoeFullDetail shoeFullDetail, HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
            if (session.getAttribute("admin") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
            }
            Shoe shoe = shoeFullDetail.getShoe();
            shoe.setCreatedBy(((Admin) session.getAttribute("admin")).getId());
            shoe.setStatus(1);
            shoeFullDetail.setShoe(shoe);

            shoeService.addShoe(shoeFullDetail);
            return ResponseEntity.ok("Shoe added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi thêm shoe, vui lòng tải lại trang, lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        ShoeFullDetail shoeFullDetail = shoeService.getShoeById(id);
        model.addAttribute("shoe", shoeFullDetail.getShoe());
        model.addAttribute("shoeDetails", shoeFullDetail.getShoeDetails());
        model.addAttribute("categories",
                categoryService.getAllCategories(-1, -1));
        model.addAttribute("contentName", "Edit Create has ID = " + id);
        model.addAttribute("content", "admin/shoe_form_edit");
        return "admin/index";
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> doEdit(@RequestBody ShoeFullDetail shoeFullDetail, @PathVariable int id,
            HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
            if (session.getAttribute("admin") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
            }

            Shoe shoe = shoeFullDetail.getShoe();
            shoe.setId(id);
            shoe.setLastUpdatedBy(((Admin) session.getAttribute("admin")).getId());
            shoe.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            shoeService.updateShoe(shoeFullDetail);

            return ResponseEntity.ok("Shoe update successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi thêm shoe, vui lòng tải lại trang, lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        shoeService.deleteShoe(id);
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Xóa thành công");
        return "redirect:/admin/shoe";
    }

    @GetMapping("/change-status/{id}")
    public String changeStatus(@PathVariable("id") int id, @RequestParam int status, HttpSession session,
            @RequestParam(name = "page", defaultValue = "1") String page,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        try {
            int res = shoeService.changeStatus(id, status, ((Admin) session.getAttribute("admin")).getId());
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false,
                    e.getMessage());
        }
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đã đổi trạng thái");
        return "redirect:/admin/shoe?page=" + page;
    }
}