package com.example.btl.btl.services;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.btl.btl.dtos.AdminChangePassForm;
import com.example.btl.btl.models.Admin;
import com.example.btl.btl.repositories.AdminRepo;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Admin> getAllAdmins(Pageable pageable) {
        return adminRepo.findAll(pageable);
    }

    public Admin getAdminById(String id) {
        return adminRepo.findById(id).orElse(null);
    }

    public void saveAdmin(Admin admin) {
        admin.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setStatus(1);
        adminRepo.save(admin);
    }

    public void updateAdmin(String id, Admin adminDetails) {
        Admin admin = getAdminById(id);
        if (admin == null) {
            return;
        }
        admin.setLastUpdatedBy(adminDetails.getLastUpdatedBy());
        admin.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        admin.setName(adminDetails.getName());
        admin.setRole(adminDetails.getRole());
        admin.setAddress(adminDetails.getAddress());
        admin.setPhone(adminDetails.getPhone());
        admin.setStatus(1);
        adminRepo.save(admin);
    }

    public List<Admin> findAllAdmins() {
        return adminRepo.findAll();
    }

    public void deleteAdmin(String id) {
        adminRepo.deleteById(id);
    }

    public Admin getAdminByUsername(String username) {
        return adminRepo.findByUsername(username);
    }

    public void changePassword(String adminId, AdminChangePassForm form) throws Exception {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new Exception("Admin not found with id: " + adminId));

        if (!passwordEncoder.matches(form.getOldPass(), admin.getPassword())) {
            throw new Exception("Incorrect old password");
        }

        String encodedPass = passwordEncoder.encode(form.getNewPass());
        admin.setPassword(encodedPass);
        adminRepo.save(admin);
    }

    @Transactional
    public void updateStatus(String id, int status, String adminId) throws Exception {
        Admin admin = adminRepo.findById(id).orElse(null);
        if (admin == null)
            throw new Exception("Không tìm thấy admin có id = " + id);
        admin.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        admin.setLastUpdatedBy(adminId);
        admin.setStatus(status);
        adminRepo.save(admin);
    }
}