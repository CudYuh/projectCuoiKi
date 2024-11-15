package com.example.btl.btl.controllers;

import javax.servlet.http.HttpSession;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.btl.btl.dtos.CartItem;
import com.example.btl.btl.services.CategoryService;
import com.example.btl.btl.services.ShoeService;

@Controller
@RequestMapping("/gio-hang")
public class CartController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    ShoeService shoeService;

    @GetMapping("")
    public String getCart(Model model, HttpSession session) {
        HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            session.setAttribute("cart", new HashMap<Integer, Integer>());
        } else {
            BigInteger totalPrice = BigInteger.ZERO;
            List<CartItem> cartItems = new ArrayList<>();
            for (int i : cart.keySet()) {
                CartItem ci = shoeService.getCartItemByShoeDetailId(i);
                ci.setQuantity(cart.get(i));
                cartItems.add(ci);

                totalPrice = totalPrice.add((ci.getPromotePrice() != null ? ci.getPromotePrice() : ci.getPrice())
                        .multiply(BigInteger.valueOf(ci.getQuantity())));
            }
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(totalPrice);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", formatted + " VND");
        }

        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        model.addAttribute("content", "cart");
        return "index";
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody int[] shoeDetailIds, HttpSession session) {
        HashMap<Integer, Integer> cartItems = (HashMap<Integer, Integer>) session.getAttribute("cart");

        if (cartItems == null) {
            cartItems = new HashMap<>();
        }

        for (int i : shoeDetailIds) {
            if (cartItems.containsKey(i)) {
                cartItems.put(i, cartItems.get(i) + 1);
            } else {
                cartItems.put(i, 1);
            }
        }

        session.setAttribute("cart", cartItems);

        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateCart(@RequestBody List<Pair<Integer, Integer>> cartItems, HttpSession session) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        for (Pair<Integer, Integer> pair : cartItems) {
            int key = pair.getFirst();
            int value = pair.getSecond();
            hashMap.put(key, value);
        }
        session.setAttribute("cart", hashMap);

        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/del/{shoeDetailId}")
    public String removeCartItem(@PathVariable int shoeDetailId, HttpSession session) {
        HashMap<Integer, Integer> cartItems = (HashMap<Integer, Integer>) session.getAttribute("cart");

        if (cartItems != null && cartItems.containsKey(shoeDetailId)) {
            cartItems.remove(shoeDetailId);
        }

        session.setAttribute("cart", cartItems);

        return "redirect:/gio-hang";
    }
}
