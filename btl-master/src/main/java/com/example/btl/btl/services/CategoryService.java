package com.example.btl.btl.services;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.btl.btl.models.Category;
import com.example.btl.btl.repositories.CategoryRepo;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public Page<Category> getAllCategories(int page, int size) {
        if (page == -1 || size == -1) {
            return categoryRepo.findAll(PageRequest.of(0, 1000000, Sort.by("id")));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        return categoryRepo.findAll(pageable);
    }

    public Page<Category> getAllStatusIs_1_Categories(int page, int size) {
        if (page == -1 || size == -1) {
            return categoryRepo.findByStatus(1, PageRequest.of(0, 1000000, Sort.by("id")));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        Page<Category> res = categoryRepo.findByStatus(1, pageable);
        return res;
    }

    public Page<Category> getAllStatusIs_1_Categories(Pageable pageable) {
        return categoryRepo.findByStatus(1, pageable);
    }

    public List<Category> getAllStatusIs_1_Categories() {
        return categoryRepo.findByStatus(1);
    }

    public Category getCategoryById(int id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        category.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        category.setStatus(1);
        return categoryRepo.save(category);
    }

    public Category updateCategory(int id, Category categoryDetails) {
        Category category = getCategoryById(id);
        if (category == null) {
            return null;
        }
        category.setLastUpdatedBy(categoryDetails.getLastUpdatedBy());
        category.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        category.setName(categoryDetails.getName());
        category.setUrl(categoryDetails.getUrl());
        if (categoryDetails.getImage() != null && categoryDetails.getImage().length > 0)
            category.setImage(categoryDetails.getImage());
        category.setStatus(categoryDetails.getStatus());
        return categoryRepo.save(category);
    }

    public void deleteCategory(int id) {
        categoryRepo.deleteById(id);
    }

    public void changeStatus(int id, int status, String adminId) throws Exception {
        Category category = categoryRepo.findById(id).orElse(null);
        if (category != null) {
            category.setStatus(status);
            category.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
            category.setLastUpdatedBy(adminId);
            categoryRepo.save(category);
        } else {
            throw new Exception("No category has ID = " + id, null);
        }
    }

    // public List<Category> getRandomStatusIs_1_Categories(int n) {
    // List<Category> categories = categoryRepo.findByStatus(1);
    // Random random = new Random();
    // int count = categories.size();
    // if (count > n) {
    // List<Integer> indexes = random.ints(0,
    // count).distinct().limit(n).boxed().collect(Collectors.toList());
    // return indexes.stream().map(t ->
    // categories.get(t)).collect(Collectors.toList());
    // }
    // return categories;
    // }

}