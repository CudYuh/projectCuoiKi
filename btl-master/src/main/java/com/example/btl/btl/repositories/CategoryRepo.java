package com.example.btl.btl.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.btl.btl.models.Category;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    List<Category> findByStatus(int status);
    Page<Category> findByStatus(int status, Pageable pageable);
    Category findByUrl(String categoryUrl);
}