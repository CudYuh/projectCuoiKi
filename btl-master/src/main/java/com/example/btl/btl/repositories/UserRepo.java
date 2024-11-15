package com.example.btl.btl.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.btl.btl.models.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}