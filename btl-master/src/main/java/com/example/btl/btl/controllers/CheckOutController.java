package com.example.btl.btl.controllers;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.btl.btl.dtos.CartItem;
import com.example.btl.btl.dtos.CheckoutModelDTO;
import com.example.btl.btl.models.User;
import com.example.btl.btl.services.OrderService;
import com.example.btl.btl.services.ShoeService;

@Controller
public class CheckOutController {

    @Autowired
    private ShoeService shoeService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dat-hang")
    public String checkout(Model model, HttpSession session) {
        HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
            model.addAttribute("cartHasItem", false);
        } else {
            if (cart.size() <= 0) {
                model.addAttribute("cartHasItem", false);
            } else {
                model.addAttribute("cartHasItem", true);
                List<CartItem> cartItems = new ArrayList<>();
                for (int i : cart.keySet()) {
                    CartItem ci = shoeService.getCartItemByShoeDetailId(i);
                    ci.setQuantity(cart.get(i));
                    cartItems.add(ci);
                }
                model.addAttribute("cartItems", cartItems);

                BigInteger totalPromotePrice = BigInteger.ZERO;
                for (CartItem item : cartItems) {
                    if (item.getPromotePrice() != null) {
                        totalPromotePrice = totalPromotePrice
                                .add(item.getPromotePrice().multiply(BigInteger.valueOf(item.getQuantity())));
                    } else {
                        totalPromotePrice = totalPromotePrice
                                .add(item.getPrice().multiply(BigInteger.valueOf(item.getQuantity())));
                    }
                }

                DecimalFormat formatter = new DecimalFormat("#,###");
                String formatted = formatter.format(totalPromotePrice);

                model.addAttribute("total", formatted + " VND");
            }
        }

        CheckoutModelDTO data = new CheckoutModelDTO();

        User user = (User) session.getAttribute("user");
        if (user != null) {
            data.setName(user.getName());
            data.setAddress(user.getAddress());
            data.setEmail(user.getEmail());
            data.setPhone(user.getPhone());
        }
        model.addAttribute("data", data);
        model.addAttribute("content", "checkout");
        return "index";
    }

    @PostMapping("/dat-hang")
    public String placeOrder(Model model, HttpSession session,
            @ModelAttribute("data") CheckoutModelDTO data) {
        HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
            model.addAttribute("data", data);
            model.addAttribute("content", "checkout");
            return "index";
        }

        List<CartItem> cartItems = new ArrayList<>();
        for (int i : cart.keySet()) {
            CartItem ci = shoeService.getCartItemByShoeDetailId(i);
            ci.setQuantity(cart.get(i));
            cartItems.add(ci);
        }

        data.setCartItems(cartItems);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            data.setUserId(user.getId());
        }

        try {
            UUID res = orderService.createFullOrder(data);
            session.setAttribute("cart", new HashMap<Integer, Integer>());
            return "redirect:/tra-cuu-don-hang/" + res;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("data", data);
            model.addAttribute("content", "checkout");
            return "index";
        }
    }
}