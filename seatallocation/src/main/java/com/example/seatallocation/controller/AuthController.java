package com.example.seatallocation.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.seatallocation.dto.AuthStatusResponse;
import com.example.seatallocation.dto.LoginRequest;
import com.example.seatallocation.dto.LoginResponse;
import com.example.seatallocation.dto.PasswordResetPublicRequest;
import com.example.seatallocation.dto.PasswordResetRequest;
import com.example.seatallocation.service.AdminAuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminAuthService adminAuthService;

    public AuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = adminAuthService.login(request.getUsername(), request.getPassword());
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        Instant expiry = adminAuthService.getExpiry(token);
        return new LoginResponse(token, expiry == null ? null : expiry.toString());
    }

    @GetMapping("/validate")
    public AuthStatusResponse validate(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (!adminAuthService.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid or expired");
        }
        Instant expiry = adminAuthService.getExpiry(token);
        return new AuthStatusResponse(true, expiry == null ? null : expiry.toString());
    }

    @PostMapping("/reset-password")
    public void resetPassword(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @RequestBody PasswordResetRequest request) {
        if (!adminAuthService.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin login required");
        }
        boolean updated = adminAuthService.resetPassword(request.getCurrentPassword(), request.getNewPassword());
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password reset failed");
        }
    }

    @PostMapping("/reset-password-public")
    public void resetPasswordPublic(@RequestBody PasswordResetPublicRequest request) {
        boolean updated = adminAuthService.resetPasswordWithUsername(
                request.getUsername(),
                request.getCurrentPassword(),
                request.getNewPassword());
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password reset failed");
        }
    }
}
