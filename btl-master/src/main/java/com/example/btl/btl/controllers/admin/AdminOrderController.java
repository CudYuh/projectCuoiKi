package com.example.btl.btl.controllers.admin;

import java.util.UUID;

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

import com.example.btl.btl.models.Admin;
import com.example.btl.btl.models.Order;
import com.example.btl.btl.services.OrderService;
import com.example.btl.btl.utils.ControllerUtils;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<Order> dataPage = orderService.getAllOrders(pageable);

        model.addAttribute("data", dataPage.getContent());
        model.addAttribute("currentPage", dataPage.getNumber() + 1);
        model.addAttribute("totalPages", dataPage.getTotalPages());
        model.addAttribute("totalItems", dataPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("contentName", "Order Management");
        model.addAttribute("content", "admin/order");

        return "admin/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("orderFullDetail", orderService.getOrderById(UUID.fromString(id)));
        model.addAttribute("contentName", "Chi tiết đơn hàng " + id);
        model.addAttribute("content", "admin/order_detail");
        return "admin/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        try {
            orderService.deleteOrder(id);
        } catch (Exception e) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false,
                    e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/order";
        }
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Xóa thành công");
        return "redirect:/admin/order";
    }

    @GetMapping("/change-status")
    public String changeStatus(@RequestParam String id, @RequestParam int status, HttpSession session,
            @RequestParam(name = "page", defaultValue = "1") String page, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        try {
            orderService.updateOrderStatus(id, status, ((Admin) session.getAttribute("admin")).getId());
        } catch (Exception e) {
            ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, false,
                    e.getMessage());
        }
        ControllerUtils.ShowAlertAfterRedirect(redirectAttributes, true, "Đã đổi trạng thái");
        return "redirect:/admin/order?page=" + page;
    }
}
