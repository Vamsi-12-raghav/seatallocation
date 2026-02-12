package com.example.seatallocation.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private final String username;
    private volatile String password;
    private final long tokenTtlMinutes;
    private final Map<String, Instant> activeTokens = new ConcurrentHashMap<>();

    public AdminAuthService(
            @Value("${app.admin.username}") String username,
            @Value("${app.admin.password}") String password,
            @Value("${app.admin.token-ttl-minutes}") long tokenTtlMinutes) {
        this.username = username;
        this.password = password;
        this.tokenTtlMinutes = tokenTtlMinutes;
    }

    public String login(String inputUsername, String inputPassword) {
        if (!username.equals(inputUsername) || !password.equals(inputPassword)) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, Instant.now().plus(tokenTtlMinutes, ChronoUnit.MINUTES));
        return token;
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
        if (!password.equals(currentPassword)) {
            return false;
        }
        String trimmedNew = newPassword.trim();
        if (trimmedNew.isEmpty()) {
            return false;
        }
        password = trimmedNew;
        activeTokens.clear();
        return true;
    }

    public synchronized boolean resetPasswordWithUsername(
            String inputUsername,
            String currentPassword,
            String newPassword) {
        if (inputUsername == null) {
            return false;
        }
        if (!username.equals(inputUsername)) {
            return false;
        }
        return resetPassword(currentPassword, newPassword);
    }
}
