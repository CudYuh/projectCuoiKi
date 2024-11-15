package com.example.btl.btl.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.btl.btl.dtos.OrderFullDetail;
import com.example.btl.btl.services.CategoryService;
import com.example.btl.btl.services.OrderService;

@Controller
public class TrackingOrderController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    OrderService orderService;

    @GetMapping("/tra-cuu-don-hang")
    public String get(Model model) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        model.addAttribute("content", "tracking");
        return "index";
    }

    @GetMapping("/tra-cuu-don-hang/{id}")
    public String track(Model model, @PathVariable(required = false, name = "id") String id) {
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        if (id == null) {
            model.addAttribute("content", "tracking");
            return "index";
        }
        try {
            UUID orderId = UUID.fromString(id);
            OrderFullDetail order = orderService.getOrderById(orderId);
            if (order != null && order.getOrder() != null) {
                model.addAttribute("orderFullDetail", order);
                model.addAttribute("content", "trackingresult");
            } else {
                model.addAttribute("error", "Không tìm thấy đơn hàng với mã " + id);
                model.addAttribute("content", "tracking");
            }
            return "index";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Id đơn hàng không đúng");
            model.addAttribute("content", "tracking");
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("content", "500");
            return "index";
        }
    }
}
