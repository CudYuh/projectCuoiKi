package com.example.btl.btl.services;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.btl.btl.dtos.UserChangePassForm;
import com.example.btl.btl.models.User;
import com.example.btl.btl.repositories.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public User getUserById(String id) {
        return userRepo.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        user.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        return userRepo.save(user);
    }

    public User updateUser(String id, User userDetails, boolean isFromUser) {
        User user = getUserById(id);
        if (user == null) {
            return null;
        }

        if (isFromUser) {
            user.setLastUpdatedBy("self");
            user.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
            user.setName(userDetails.getName());
            user.setAddress(userDetails.getAddress());
            user.setPhone(userDetails.getPhone());
            user.setEmail(userDetails.getEmail());
            return userRepo.save(user);
        }
        user.setLastUpdatedBy(userDetails.getLastUpdatedBy());
        user.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        user.setName(userDetails.getName());
        user.setAddress(userDetails.getAddress());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setStatus(1);
        return userRepo.save(user);
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void changePassword(String userId, UserChangePassForm form) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User not found with id: " + userId));

        if (!passwordEncoder.matches(form.getOldPass(), user.getPassword())) {
            throw new Exception("Incorrect old password");
        }

        String encodedPass = passwordEncoder.encode(form.getNewPass());
        user.setPassword(encodedPass);
        userRepo.save(user);
    }

    @Transactional
    public void updateStatus(String id, int status, String userId) throws Exception {
        User user = userRepo.findById(id).orElse(null);
        if (user == null)
            throw new Exception("Không tìm thấy user có id = " + id);
        user.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        user.setLastUpdatedBy(userId);
        user.setStatus(status);
        userRepo.save(user);
    }
}