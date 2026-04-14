package com.example.seatallocation.dto;

public class AuthStatusResponse {
    private boolean valid;
    private String expiresAt;

    public AuthStatusResponse(boolean valid, String expiresAt) {
        this.valid = valid;
        this.expiresAt = expiresAt;
    }

    public boolean isValid() {
        return valid;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}
