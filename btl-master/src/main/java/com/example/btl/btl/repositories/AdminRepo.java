package com.example.btl.btl.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.btl.btl.models.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<Admin, String> {
    Admin findByUsername(String username);
    Page<Admin> findAll(Pageable pageable);
    List<Admin> findAll();
}