package com.example.seatallocation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seatallocation.dto.AdminCredentialResponse;
import com.example.seatallocation.dto.AdminRegistrationRequest;
import com.example.seatallocation.entity.Admin;
import com.example.seatallocation.repository.AdminRepository;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    /**
     * Register a new admin member
     */
    public AdminCredentialResponse registerAdmin(AdminRegistrationRequest request) {
        // Check if username already exists
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new admin
        Admin admin = new Admin(
            request.getUsername(),
            request.getPassword(),
            request.getEmail(),
            request.getFullName(),
            request.getDepartment()
        );
        
        Admin savedAdmin = adminRepository.save(admin);
        return convertToCredentialResponse(savedAdmin);
    }
    
    /**
     * Get admin credentials by username
     */
    public AdminCredentialResponse getCredentialsByUsername(String username) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return convertToCredentialResponse(admin.get());
        }
        throw new RuntimeException("Admin not found");
    }
    
    /**
     * Get admin credentials by email
     */
    public AdminCredentialResponse getCredentialsByEmail(String email) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return convertToCredentialResponse(admin.get());
        }
        throw new RuntimeException("Admin not found");
    }
    
    /**
     * Get admin by ID
     */
    public AdminCredentialResponse getAdminById(Long id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isPresent()) {
            return convertToCredentialResponse(admin.get());
        }
        throw new RuntimeException("Admin not found");
    }
    
    /**
     * Get all active admins
     */
    public List<AdminCredentialResponse> getAllActiveAdmins() {
        return adminRepository.findAll().stream()
            .filter(Admin::getActive)
            .map(this::convertToCredentialResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Update admin information
     */
    public AdminCredentialResponse updateAdmin(Long id, AdminRegistrationRequest request) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (!adminOpt.isPresent()) {
            throw new RuntimeException("Admin not found");
        }
        
        Admin admin = adminOpt.get();
        
        // Check if new username is unique (if being changed)
        if (!admin.getUsername().equals(request.getUsername()) && 
            adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if new email is unique (if being changed)
        if (!admin.getEmail().equals(request.getEmail()) && 
            adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        admin.setUsername(request.getUsername());
        admin.setPassword(request.getPassword());
        admin.setEmail(request.getEmail());
        admin.setFullName(request.getFullName());
        admin.setDepartment(request.getDepartment());
        
        Admin updatedAdmin = adminRepository.save(admin);
        return convertToCredentialResponse(updatedAdmin);
    }
    
    /**
     * Deactivate an admin
     */
    public void deactivateAdmin(Long id) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setActive(false);
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("Admin not found");
        }
    }
    
    /**
     * Activate an admin
     */
    public void activateAdmin(Long id) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setActive(true);
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("Admin not found");
        }
    }
    
    /**
     * Convert Admin entity to CredentialResponse DTO
     */
    private AdminCredentialResponse convertToCredentialResponse(Admin admin) {
        return new AdminCredentialResponse(
            admin.getAdminId(),
            admin.getUsername(),
            admin.getPassword(),
            admin.getEmail(),
            admin.getFullName(),
            admin.getDepartment(),
            admin.getActive(),
            admin.getCreatedAt()
        );
    }
}
