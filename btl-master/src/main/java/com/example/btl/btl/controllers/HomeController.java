package com.example.btl.btl.controllers;

import javax.servlet.http.HttpServletRequest;
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

import com.example.btl.btl.dtos.ShoeFullDetail;
import com.example.btl.btl.models.Shoe;
import com.example.btl.btl.services.CategoryService;
import com.example.btl.btl.services.ShoeService;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShoeService shoeService;

    @GetMapping
    public String index(Model model,HttpSession session) {
        model.addAttribute("newShoes", shoeService.getNewestShoes(3));
        model.addAttribute("mostBuyShoes", shoeService.getMostBuy(3));
        model.addAttribute("saleShoes", shoeService.getMostPromoteShoes(3));

        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        model.addAttribute("content", "home");
        return "index";
    }

    @GetMapping("/danh-sach-san-pham")
    public String listShoes(@RequestParam(required = false, name = "danh-muc") String categoryURL,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "newest") String filter, Model model, HttpServletRequest request) {
        int size = 9;
        Sort sort = null;
        String filterName = null;
        switch (filter) {
            case "newest":
                sort = Sort.by(Sort.Order.desc("createdAt"));
                filterName = "Mới nhất";
                break;
            case "oldest":
                sort = Sort.by(Sort.Order.asc("createdAt"));
                filterName = "Cũ nhất";
            case "az":
                sort = Sort.by(Sort.Order.asc("title"));
                filterName = "Tên: A đến Z";
                break;
            case "za":
                sort = Sort.by(Sort.Order.desc("title"));
                filterName = "Tên: Z đến A";
                break;
            case "lowhigh":
                sort = Sort.by(Sort.Order.asc("promotePrice"), Sort.Order.asc("price"));
                filterName = "Giá: Thấp đến cao";
                break;
            case "highlow":
                sort = Sort.by(Sort.Order.desc("promotePrice"), Sort.Order.desc("price"));
                filterName = "Giá: Cao đến thấp";
                break;
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Shoe> dataPage = null;

        if (categoryURL != null) {
            if (!categoryURL.isEmpty()) {
                dataPage = shoeService.getByCategoryURL(categoryURL, pageable);
            }
        } else {
            dataPage = shoeService.getAllShoes(pageable);
        }

        String uri = request.getRequestURI();
        String currentPath = request.getQueryString() != null ? uri.replace(request.getQueryString(), "") : uri;

        model.addAttribute("data", dataPage.getContent());
        model.addAttribute("currentPage", dataPage.getNumber() + 1);
        model.addAttribute("totalPages", dataPage.getTotalPages());
        model.addAttribute("totalItems", dataPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("categoryURL", categoryURL);
        model.addAttribute("currentPath", currentPath);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("currentFilterName", filterName);

        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(-1, -1));
        model.addAttribute("content", "list");
        return "index";
    }

    @GetMapping("/tim-kiem")
    public String search(@RequestParam(required = false, name = "search_query") String search_query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "newest") String filter, Model model, HttpServletRequest request) {

        if (!search_query.isEmpty()) {
            int size = 9;
            Sort sort = null;
            String filterName = null;
            switch (filter) {
                case "newest":
                    sort = Sort.by(Sort.Order.desc("createdAt"));
                    filterName = "Mới nhất";
                    break;
                case "oldest":
                    sort = Sort.by(Sort.Order.asc("createdAt"));
                    filterName = "Cũ nhất";
                case "az":
                    sort = Sort.by(Sort.Order.asc("title"));
                    filterName = "Tên: A đến Z";
                    break;
                case "za":
                    sort = Sort.by(Sort.Order.desc("title"));
                    filterName = "Tên: Z đến A";
                    break;
                case "lowhigh":
                    sort = Sort.by(Sort.Order.asc("promotePrice"), Sort.Order.asc("price"));
                    filterName = "Giá: Thấp đến cao";
                    break;
                case "highlow":
                    sort = Sort.by(Sort.Order.desc("promotePrice"), Sort.Order.desc("price"));
                    filterName = "Giá: Cao đến thấp";
                    break;
            }
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Shoe> dataPage = null;

            if (search_query != null) {
                if (!search_query.isEmpty()) {
                    dataPage = shoeService.search(search_query, pageable);
                }
            }

            String uri = request.getRequestURI();
            String currentPath = request.getQueryString() != null ? uri.replace(request.getQueryString(), "") : uri;

            model.addAttribute("data", dataPage.getContent());
            model.addAttribute("currentPage", dataPage.getNumber() + 1);
            model.addAttribute("totalPages", dataPage.getTotalPages());
            model.addAttribute("totalItems", dataPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("search_query", search_query);
            model.addAttribute("currentPath", currentPath);
            model.addAttribute("currentFilter", filter);
            model.addAttribute("currentFilterName", filterName);
        }

        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(-1, -1));
        model.addAttribute("content", "search");
        return "index";
    }

    @GetMapping("/san-pham/{shoeid}")
    public String shoeDetail(@PathVariable int shoeid, Model model) {
        ShoeFullDetail shoeFullDetail = shoeService.getShoeById(shoeid);
        model.addAttribute("shoe", shoeFullDetail.getShoe());
        model.addAttribute("shoeDetails", shoeFullDetail.getShoeDetails());
        model.addAttribute("categories", categoryService.getAllStatusIs_1_Categories(1, 20));
        model.addAttribute("content", "detail");
        return "index";
    }
}