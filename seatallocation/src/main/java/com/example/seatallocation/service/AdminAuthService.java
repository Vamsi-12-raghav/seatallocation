package com.example.seatallocation.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.seatallocation.entity.Admin;
import com.example.seatallocation.repository.AdminRepository;

@Service
public class AdminAuthService {

    @Autowired
    private AdminRepository adminRepository;
    
    private final String defaultUsername;
    private volatile String defaultPassword;
    private final long tokenTtlMinutes;
    private final Map<String, Instant> activeTokens = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToUsername = new ConcurrentHashMap<>();

    public AdminAuthService(
            @Value("${app.admin.username}") String defaultUsername,
            @Value("${app.admin.password}") String defaultPassword,
            @Value("${app.admin.token-ttl-minutes}") long tokenTtlMinutes) {
        this.defaultUsername = defaultUsername;
        this.defaultPassword = defaultPassword;
        this.tokenTtlMinutes = tokenTtlMinutes;
    }

    public String login(String inputUsername, String inputPassword) {
        if (inputUsername == null || inputPassword == null) {
            return null;
        }

        // First, try to authenticate against the Admin table
        try {
            if (adminRepository != null) {
                Optional<Admin> admin = adminRepository.findByUsername(inputUsername);
                if (admin.isPresent()) {
                    Admin foundAdmin = admin.get();
                    System.out.println("DEBUG: Found admin in database: " + inputUsername);
                    System.out.println("DEBUG: Admin active status: " + foundAdmin.getActive());
                    System.out.println("DEBUG: Password match: " + foundAdmin.getPassword().equals(inputPassword));
                    
                    if (foundAdmin.getActive() && foundAdmin.getPassword().equals(inputPassword)) {
                        String token = UUID.randomUUID().toString();
                        activeTokens.put(token, Instant.now().plus(tokenTtlMinutes, ChronoUnit.MINUTES));
                        tokenToUsername.put(token, inputUsername);
                        System.out.println("Login successful for admin user: " + inputUsername);
                        return token;
                    }
                    System.out.println("Admin verification failed for user: " + inputUsername);
                    return null;
                }
                System.out.println("DEBUG: Admin not found in database for user: " + inputUsername);
            }
        } catch (Exception e) {
            System.err.println("Error checking admin database: " + e.getMessage());
            e.printStackTrace();
        }

        // Fall back to default hardcoded credentials (for backward compatibility)
        System.out.println("Trying default credentials for user: " + inputUsername);
        if (defaultUsername.equals(inputUsername) && defaultPassword.equals(inputPassword)) {
            String token = UUID.randomUUID().toString();
            activeTokens.put(token, Instant.now().plus(tokenTtlMinutes, ChronoUnit.MINUTES));
            tokenToUsername.put(token, inputUsername);
            System.out.println("Login successful using default credentials");
            return token;
        }

        System.out.println("Login failed for user: " + inputUsername);
        return null;
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        Instant expiry = activeTokens.get(token);
        if (expiry == null) {
            return false;
        }
        if (Instant.now().isAfter(expiry)) {
            activeTokens.remove(token);
            tokenToUsername.remove(token);
            return false;
        }
        return true;
    }

    public Instant getExpiry(String token) {
        return activeTokens.get(token);
    }

    public synchronized boolean resetPassword(String currentPassword, String newPassword) {
        if (currentPassword == null || newPassword == null) {
            return false;
        }
        if (!defaultPassword.equals(currentPassword)) {
            return false;
        }
        String trimmedNew = newPassword.trim();
        if (trimmedNew.isEmpty()) {
            return false;
        }
        defaultPassword = trimmedNew;
        activeTokens.clear();
        tokenToUsername.clear();
        return true;
    }

    public synchronized boolean resetPasswordWithUsername(
            String inputUsername,
            String currentPassword,
            String newPassword) {
        if (inputUsername == null || currentPassword == null || newPassword == null) {
            return false;
        }

        // Try to reset password for admin in the Admin table
        Optional<Admin> admin = adminRepository.findByUsername(inputUsername);
        if (admin.isPresent()) {
            Admin foundAdmin = admin.get();
            if (foundAdmin.getPassword().equals(currentPassword)) {
                String trimmedNew = newPassword.trim();
                if (!trimmedNew.isEmpty()) {
                    foundAdmin.setPassword(trimmedNew);
                    adminRepository.save(foundAdmin);
                    activeTokens.clear();
                    tokenToUsername.clear();
                    return true;
                }
            }
            return false;
        }

        // Fall back to default credentials
        if (!defaultUsername.equals(inputUsername)) {
            return false;
        }
        return resetPassword(currentPassword, newPassword);
    }
}
